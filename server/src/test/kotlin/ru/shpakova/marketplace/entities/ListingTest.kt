package ru.shpakova.marketplace.entities

import ru.shpakova.marketplace.entities.listing.Listing
import ru.shpakova.marketplace.entities.listing.ListingStatus
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ListingTest {
    @Test
    fun newListingStartsAsAvailable() {
        val listing = listing()

        assertEquals(ListingStatus.AVAILABLE, listing.status)
    }

    @Test
    fun reservationCanBePlacedAndReleased() {
        val listing = listing()

        listing.reserve()
        assertEquals(ListingStatus.RESERVED, listing.status)

        listing.releaseReservation()
        assertEquals(ListingStatus.AVAILABLE, listing.status)
    }

    @Test
    fun reservedListingCanBeMarkedAsSold() {
        val listing = listing()

        listing.reserve()
        listing.markAsSold()

        assertEquals(ListingStatus.SOLD, listing.status)
    }

    @Test
    fun cannotReserveListingTwice() {
        val listing = listing()
        listing.reserve()

        assertFailsWith<IllegalStateException> { listing.reserve() }
    }

    @Test
    fun cannotReleaseAnAvailableListing() {
        assertFailsWith<IllegalStateException> { listing().releaseReservation() }
    }

    @Test
    fun cannotSellAnAvailableListing() {
        assertFailsWith<IllegalStateException> { listing().markAsSold() }
    }

    @Test
    fun titleMustNotBeBlank() {
        assertFailsWith<IllegalArgumentException> { listing(title = "  ") }
    }

    @Test
    fun priceMustNotBeNegative() {
        assertFailsWith<IllegalArgumentException> { listing(price = BigDecimal("-0.01")) }
    }

    private fun listing(
        title: String = "Book",
        price: BigDecimal = BigDecimal("10.00"),
    ) = Listing(
        id = UUID.randomUUID(),
        sellerId = UUID.randomUUID(),
        title = title,
        description = "A book",
        price = price,
    )
}
