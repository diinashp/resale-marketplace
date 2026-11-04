package ru.shpakova.marketplace.domain.reservation.states

import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException

class ReservationCancelledState(): ReservationState {
    override fun reservationStatus(): ReservationStatus {
        return ReservationStatus.CANCELLED
    }

    override fun cancelReservation(reservation: Reservation): Reservation {
        throw InvalidStateTransitionException("Cannot cancel reservation: it has already been cancelled")
    }

    override fun completeReservation(reservation: Reservation): Reservation {
        throw InvalidStateTransitionException("Cannot confirm receipt: reservation has been cancelled")
    }
}
