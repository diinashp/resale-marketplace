package ru.shpakova.marketplace.domain.reservation

import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import ru.shpakova.marketplace.domain.reservation.states.ReservationActiveState
import ru.shpakova.marketplace.domain.reservation.states.ReservationCancelledState
import ru.shpakova.marketplace.domain.reservation.states.ReservationCompletedState
import ru.shpakova.marketplace.domain.reservation.states.ReservationState

private fun Reservation.currentState(): ReservationState {
    return when (status) {
        ReservationStatus.ACTIVE -> ReservationActiveState()
        ReservationStatus.CANCELLED -> ReservationCancelledState()
        ReservationStatus.COMPLETED -> ReservationCompletedState()
    }
}

fun Reservation.cancelReservation(): Reservation {
    return currentState().cancelReservation(this)
}

fun Reservation.completeReservation(): Reservation {
    return currentState().completeReservation(this)
}