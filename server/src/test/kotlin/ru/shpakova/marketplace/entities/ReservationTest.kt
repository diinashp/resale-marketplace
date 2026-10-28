package ru.shpakova.marketplace.entities

import ru.shpakova.marketplace.entities.reservation.Reservation
import ru.shpakova.marketplace.entities.reservation.ReservationStatus
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
        val reservation = reservation()

        reservation.cancel()

        assertEquals(ReservationStatus.CANCELLED, reservation.status)
    }

    @Test
    fun activeReservationCanBeCompleted() {
        val reservation = reservation()

        reservation.complete()

        assertEquals(ReservationStatus.COMPLETED, reservation.status)
    }

    @Test
    fun completedReservationCannotBeCancelled() {
        val reservation = reservation()
        reservation.complete()

        assertFailsWith<IllegalStateException> { reservation.cancel() }
    }

    @Test
    fun cancelledReservationCannotBeCompleted() {
        val reservation = reservation()
        reservation.cancel()

        assertFailsWith<IllegalStateException> { reservation.complete() }
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
