package ru.shpakova.marketplace.domain.listing

import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ListingTest {
    @Test
    fun newListingStartsAsAvailable() {
        assertEquals(ListingStatus.AVAILABLE, listing().status)
    }

    @Test
    fun reservationCanBePlacedAndReleased() {
        val original = listing()

        val reserved = original.reserveListing()
        val released = reserved.releaseReservation()

        assertEquals(ListingStatus.AVAILABLE, original.status)
        assertEquals(ListingStatus.RESERVED, reserved.status)
        assertEquals(ListingStatus.AVAILABLE, released.status)
    }

    @Test
    fun reservedListingCanBeMarkedAsSold() {
        val sold = listing().reserveListing().markAsSold()

        assertEquals(ListingStatus.SOLD, sold.status)
    }

    @Test
    fun cannotReserveListingTwice() {
        val reserved = listing().reserveListing()

        assertFailsWith<InvalidStateTransitionException> { reserved.reserveListing() }
    }

    @Test
    fun cannotReleaseAnAvailableListing() {
        assertFailsWith<InvalidStateTransitionException> { listing().releaseReservation() }
    }

    @Test
    fun cannotSellAnAvailableListing() {
        assertFailsWith<InvalidStateTransitionException> { listing().markAsSold() }
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
