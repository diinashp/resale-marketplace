package ru.shpakova.marketplace.domain.reservation

import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReservationTest {
    @Test
    fun newReservationStartsAsActive() {
        assertEquals(ReservationStatus.ACTIVE, reservation().status)
    }

    @Test
    fun activeReservationCanBeCancelled() {
        val original = reservation()

        val cancelled = original.cancelReservation()

        assertEquals(ReservationStatus.ACTIVE, original.status)
        assertEquals(ReservationStatus.CANCELLED, cancelled.status)
    }

    @Test
    fun activeReservationCanBeCompleted() {
        val original = reservation()

        val completed = original.completeReservation()

        assertEquals(ReservationStatus.ACTIVE, original.status)
        assertEquals(ReservationStatus.COMPLETED, completed.status)
    }

    @Test
    fun completedReservationCannotBeCancelled() {
        val completed = reservation().completeReservation()

        assertFailsWith<InvalidStateTransitionException> { completed.cancelReservation() }
    }

    @Test
    fun cancelledReservationCannotBeCompleted() {
        val cancelled = reservation().cancelReservation()

        assertFailsWith<InvalidStateTransitionException> { cancelled.completeReservation() }
    }

    @Test
    fun sellerCannotReserveTheirOwnListing() {
        val sameUserId = UUID.randomUUID()

        assertFailsWith<IllegalArgumentException> {
            Reservation(
                listingId = UUID.randomUUID(),
                buyerId = sameUserId,
                sellerId = sameUserId,
            )
        }
    }

    private fun reservation() = Reservation(
        listingId = UUID.randomUUID(),
        buyerId = UUID.randomUUID(),
        sellerId = UUID.randomUUID(),
    )
}
