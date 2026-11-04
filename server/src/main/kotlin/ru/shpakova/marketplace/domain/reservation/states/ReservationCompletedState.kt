package ru.shpakova.marketplace.domain.reservation.states

import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException

class ReservationCompletedState(): ReservationState {
    override fun reservationStatus(): ReservationStatus {
        return ReservationStatus.COMPLETED
    }

    override fun cancelReservation(reservation: Reservation): Reservation {
        throw InvalidStateTransitionException("Cannot cancel reservation: it has already been completed")
    }

    override fun completeReservation(reservation: Reservation): Reservation {
        throw InvalidStateTransitionException("Cannot confirm receipt: reservation has already been completed")
    }
}
