package ru.shpakova.marketplace.services

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.reserveListing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import ru.shpakova.marketplace.exceptions.ListingNotFoundException
import ru.shpakova.marketplace.exceptions.ReservationNotFoundException
import ru.shpakova.marketplace.repositories.ListingRepository
import ru.shpakova.marketplace.repositories.ReservationRepository
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MarketplaceServicesTest {
    private val listings = FakeListingRepository()
    private val reservations = FakeReservationRepository()
    private val listingService = ListingService(listings)
    private val reservationService = ReservationService(reservations, listings)

    @Test
    fun createListingSavesItForTheActingSeller() {
        val sellerId = UUID.randomUUID()

        val created = listingService.createListing(sellerId, "Book", "Used book", BigDecimal("12.50"))

        assertEquals(sellerId, created.sellerId)
        assertEquals(created, listings.getById(created.id))
    }

    @Test
    fun getListingReportsMissingId() {
        val missingId = UUID.randomUUID()

        assertFailsWith<ListingNotFoundException> { listingService.getListing(missingId) }
    }

    @Test
    fun sellerListingsAreFilteredBySellerAndStatus() {
        val sellerId = UUID.randomUUID()
        val available = listing(sellerId)
        val reserved = listing(sellerId).reserveListing()
        val anotherSeller = listing(UUID.randomUUID())
        listOf(available, reserved, anotherSeller).forEach(listings::save)

        val result = listingService.getSellerListingsByStatus(sellerId, ListingStatus.AVAILABLE)

        assertEquals(listOf(available), result)
    }

    @Test
    fun sellerCannotDeleteAnotherSellersListing() {
        val ownerId = UUID.randomUUID()
        val listing = listing(ownerId)
        listings.save(listing)

        assertFailsWith<IllegalArgumentException> {
            listingService.deleteListing(UUID.randomUUID(), listing.id)
        }
        assertEquals(listing, listings.getById(listing.id))
    }

    @Test
    fun placingReservationReservesAndPersistsBothRecords() {
        val sellerId = UUID.randomUUID()
        val buyerId = UUID.randomUUID()
        val listing = listing(sellerId)
        listings.save(listing)

        val reservation = reservationService.placeReservation(buyerId, listing.id)

        assertEquals(ListingStatus.AVAILABLE, listing.status)
        assertEquals(ListingStatus.RESERVED, listings.getById(listing.id)?.status)
        assertEquals(ReservationStatus.ACTIVE, reservation.status)
        assertEquals(reservation, reservations.getById(reservation.id))
    }

    @Test
    fun cannotReserveOwnListingAndListingRemainsAvailable() {
        val listing = listing(UUID.randomUUID())
        listings.save(listing)

        assertFailsWith<IllegalArgumentException> {
            reservationService.placeReservation(listing.sellerId, listing.id)
        }
        assertEquals(ListingStatus.AVAILABLE, listings.getById(listing.id)?.status)
        assertTrue(reservations.all().isEmpty())
    }

    @Test
    fun cannotReserveMissingListing() {
        assertFailsWith<ListingNotFoundException> {
            reservationService.placeReservation(UUID.randomUUID(), UUID.randomUUID())
        }
    }

    @Test
    fun cancellingReservationReleasesListing() {
        val (listing, reservation) = activeReservation()

        reservationService.cancelReservation(reservation.buyerId, reservation.id)

        assertEquals(ListingStatus.AVAILABLE, listings.getById(listing.id)?.status)
        assertEquals(ReservationStatus.CANCELLED, reservations.getById(reservation.id)?.status)
    }

    @Test
    fun onlyReservationParticipantsCanCancel() {
        val (listing, reservation) = activeReservation()

        assertFailsWith<IllegalArgumentException> {
            reservationService.cancelReservation(UUID.randomUUID(), reservation.id)
        }

        assertEquals(ListingStatus.RESERVED, listings.getById(listing.id)?.status)
        assertEquals(ReservationStatus.ACTIVE, reservations.getById(reservation.id)?.status)
    }

    @Test
    fun confirmingReceiptCompletesReservationAndSellsListing() {
        val (listing, reservation) = activeReservation()

        reservationService.confirmReceipt(reservation.buyerId, reservation.id)

        assertEquals(ListingStatus.SOLD, listings.getById(listing.id)?.status)
        assertEquals(ReservationStatus.COMPLETED, reservations.getById(reservation.id)?.status)
    }

    @Test
    fun onlyBuyerCanConfirmReceipt() {
        val (listing, reservation) = activeReservation()

        assertFailsWith<IllegalArgumentException> {
            reservationService.confirmReceipt(reservation.sellerId, reservation.id)
        }

        assertEquals(ListingStatus.RESERVED, listings.getById(listing.id)?.status)
        assertEquals(ReservationStatus.ACTIVE, reservations.getById(reservation.id)?.status)
    }

    @Test
    fun getReservationReportsMissingId() {
        assertFailsWith<ReservationNotFoundException> {
            reservationService.getReservation(UUID.randomUUID())
        }
    }

    private fun activeReservation(): Pair<Listing, Reservation> {
        val listing = listing(UUID.randomUUID())
        listings.save(listing)
        val reservation = reservationService.placeReservation(UUID.randomUUID(), listing.id)
        return listings.getById(listing.id)!! to reservations.getById(reservation.id)!!
    }

    private fun listing(sellerId: UUID) = Listing(
        sellerId = sellerId,
        title = "Book",
        description = "Used book",
        price = BigDecimal("10.00"),
    )

    private class FakeListingRepository : ListingRepository {
        private val data = linkedMapOf<UUID, Listing>()

        override fun save(listing: Listing) { data[listing.id] = listing }
        override fun getById(id: UUID): Listing? = data[id]
        override fun getByIdForUpdate(id: UUID): Listing? = data[id]
        override fun getAllBySellerIdAndStatus(sellerId: UUID, status: ListingStatus) =
            data.values.filter { it.sellerId == sellerId && it.status == status }
        override fun deleteById(listingId: UUID) { data.remove(listingId) }
    }

    private class FakeReservationRepository : ReservationRepository {
        private val data = linkedMapOf<UUID, Reservation>()

        override fun save(reservation: Reservation) { data[reservation.id] = reservation }
        override fun getById(reservationId: UUID): Reservation? = data[reservationId]
        override fun getAllByBuyerId(buyerId: UUID) = data.values.filter { it.buyerId == buyerId }
        override fun getAllBySellerId(sellerId: UUID) = data.values.filter { it.sellerId == sellerId }
        fun all(): List<Reservation> = data.values.toList()
    }
}
