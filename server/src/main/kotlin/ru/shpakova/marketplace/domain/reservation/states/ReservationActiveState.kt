package ru.shpakova.marketplace.domain.reservation.states

import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus

class ReservationActiveState(): ReservationState {
    override fun reservationStatus(): ReservationStatus {
        return ReservationStatus.ACTIVE
    }

    override fun cancelReservation(reservation: Reservation): Reservation {
        return reservation.copy(status = ReservationStatus.CANCELLED)
    }

    override fun completeReservation(reservation: Reservation): Reservation {
        return reservation.copy(status = ReservationStatus.COMPLETED)
    }
}