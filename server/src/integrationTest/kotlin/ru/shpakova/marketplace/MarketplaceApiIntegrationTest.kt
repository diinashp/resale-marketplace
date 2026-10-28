package ru.shpakova.marketplace

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MarketplaceApiIntegrationTest {
    @LocalServerPort
    private var port: Int = 0

    private val http = HttpClient.newHttpClient()

    @AfterAll
    fun stopDatabase() {
        postgres.stop()
    }

    @Test
    fun createListingValidatesRequestAndCanBeRetrieved() {
        val sellerId = UUID.randomUUID()
        val created = post("/api/listing", listingJson(sellerId, title = "Camera"))

        assertEquals(201, created.statusCode())
        assertTrue(created.body().contains("\"title\":\"Camera\""), created.body())
        assertTrue(created.body().contains("\"status\":\"AVAILABLE\""), created.body())

        val listingId = UUID.fromString(jsonValue(created.body(), "id"))
        val fetched = get("/api/listing/$listingId")

        assertEquals(200, fetched.statusCode())
        assertTrue(fetched.body().contains(listingId.toString()), fetched.body())
    }

    @Test
    fun invalidListingRequestReturnsBadRequest() {
        val body = """{"sellerId":"${UUID.randomUUID()}","title":" ","description":"x","price":-1}"""

        val response = post("/api/listing", body)

        assertEquals(400, response.statusCode())
        assertTrue(response.body().contains("title"), response.body())
        assertTrue(response.body().contains("price"), response.body())
    }

    @Test
    fun listingTitleLongerThan50CharactersReturnsBadRequest() {
        val response = post("/api/listing", listingJson(UUID.randomUUID(), title = "x".repeat(51)))

        assertEquals(400, response.statusCode())
        assertTrue(response.body().contains("title"), response.body())
    }

    @Test
    fun sellerCanFilterListingsByStatus() {
        val sellerId = UUID.randomUUID()
        val available = post("/api/listing", listingJson(sellerId, title = "Available"))
        val reservedListing = post("/api/listing", listingJson(sellerId, title = "Reserved"))
        assertEquals(201, available.statusCode())
        assertEquals(201, reservedListing.statusCode())
        val listingId = jsonValue(reservedListing.body(), "id")
        assertEquals(201, post("/api/reservations", reservationJson(listingId, UUID.randomUUID())).statusCode())

        val response = get("/api/listing?sellerId=$sellerId&status=RESERVED")

        assertEquals(200, response.statusCode())
        assertTrue(response.body().contains("Reserved"), response.body())
        assertTrue(!response.body().contains("Available"), response.body())
    }

    @Test
    fun bookingCancellationAndReceiptConfirmationUpdateBothRecords() {
        val sellerId = UUID.randomUUID()
        val buyerId = UUID.randomUUID()

        val cancelledListingId = createListing(sellerId, "Cancel me")
        val cancelledReservation = post("/api/reservations", reservationJson(cancelledListingId, buyerId))
        assertEquals(201, cancelledReservation.statusCode())
        val cancelledReservationId = jsonValue(cancelledReservation.body(), "id")

        val cancelResponse = post("/api/reservations/$cancelledReservationId/cancel?actorUserId=$buyerId", "")
        assertEquals(204, cancelResponse.statusCode())
        assertTrue(get("/api/reservations/$cancelledReservationId").body().contains("CANCELLED"))
        assertTrue(get("/api/listing/$cancelledListingId").body().contains("AVAILABLE"))

        val soldListingId = createListing(sellerId, "Sell me")
        val activeReservation = post("/api/reservations", reservationJson(soldListingId, buyerId))
        assertEquals(201, activeReservation.statusCode())
        val reservationId = jsonValue(activeReservation.body(), "id")

        assertTrue(get("/api/reservations/buyer?buyerId=$buyerId").body().contains(reservationId))
        assertTrue(get("/api/reservations/seller?sellerId=$sellerId").body().contains(reservationId))

        val confirmResponse = post("/api/reservations/$reservationId/confirm-receipt?actorUserId=$buyerId", "")
        assertEquals(204, confirmResponse.statusCode())
        assertTrue(get("/api/reservations/$reservationId").body().contains("COMPLETED"))
        assertTrue(get("/api/listing/$soldListingId").body().contains("SOLD"))
    }

    @Test
    fun secondBookingOfReservedListingReturnsConflict() {
        val listingId = createListing(UUID.randomUUID(), "Single copy")

        assertEquals(201, post("/api/reservations", reservationJson(listingId, UUID.randomUUID())).statusCode())
        val secondAttempt = post("/api/reservations", reservationJson(listingId, UUID.randomUUID()))

        assertEquals(409, secondAttempt.statusCode())
        assertTrue(secondAttempt.body().contains("CONFLICT"), secondAttempt.body())
    }

    @Test
    fun simultaneousBookingsCreateOnlyOneReservation() {
        val listingId = createListing(UUID.randomUUID(), "One item")
        val startTogether = CountDownLatch(1)
        val ready = CountDownLatch(2)
        val pool = Executors.newFixedThreadPool(2)

        try {
            val attempts = (1..2).map {
                pool.submit<Int> {
                    ready.countDown()
                    check(ready.await(10, TimeUnit.SECONDS)) { "Both requests did not get ready" }
                    startTogether.await()
                    post("/api/reservations", reservationJson(listingId, UUID.randomUUID())).statusCode()
                }
            }
            assertTrue(ready.await(10, TimeUnit.SECONDS), "Requests were not ready in time")
            startTogether.countDown()

            val statuses = attempts.map { it.get(30, TimeUnit.SECONDS) }.sorted()
            assertEquals(listOf(201, 409), statuses)
        } finally {
            startTogether.countDown()
            pool.shutdownNow()
        }
    }

    @Test
    fun unknownListingReturnsNotFound() {
        val response = get("/api/listing/${UUID.randomUUID()}")

        assertEquals(404, response.statusCode())
        assertTrue(response.body().contains("NOT_FOUND"), response.body())
    }

    private fun createListing(sellerId: UUID, title: String): String {
        val response = post("/api/listing", listingJson(sellerId, title))
        assertEquals(201, response.statusCode(), response.body())
        return jsonValue(response.body(), "id")
    }

    private fun listingJson(sellerId: UUID, title: String) =
        """{"sellerId":"$sellerId","title":"$title","description":"Test item","price":25.50}"""

    private fun reservationJson(listingId: String, buyerId: UUID) =
        """{"listingId":"$listingId","buyerId":"$buyerId"}"""

    private fun post(path: String, body: String): HttpResponse<String> = request("POST", path, body)

    private fun get(path: String): HttpResponse<String> = request("GET", path)

    private fun request(method: String, path: String, body: String = ""): HttpResponse<String> {
        val builder = HttpRequest.newBuilder(URI("http://localhost:$port$path"))
            .header("Content-Type", "application/json")
        val request = if (method == "POST") {
            builder.POST(HttpRequest.BodyPublishers.ofString(body)).build()
        } else {
            builder.GET().build()
        }
        return http.send(request, HttpResponse.BodyHandlers.ofString())
    }

    private fun jsonValue(json: String, key: String): String {
        val match = Regex("\\\"$key\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").find(json)
            ?: error("Property '$key' was not found in JSON: $json")
        return match.groupValues[1]
    }

    companion object {
        val postgres = PostgreSQLContainer<Nothing>("postgres:17-alpine").apply { start() }

        @JvmStatic
        @DynamicPropertySource
        fun databaseProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }
}
