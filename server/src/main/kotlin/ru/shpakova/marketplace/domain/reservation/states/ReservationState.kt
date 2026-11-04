package ru.shpakova.marketplace.domain.reservation.states

import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus

interface ReservationState {
    fun reservationStatus(): ReservationStatus

    fun cancelReservation(reservation: Reservation) : Reservation

    fun completeReservation(reservation: Reservation) : Reservation
}