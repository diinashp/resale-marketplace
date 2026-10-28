package ru.shpakova.marketplace.repositories

import ru.shpakova.marketplace.entities.reservation.Reservation
import java.util.UUID

interface ReservationRepository {
    fun save(reservation: Reservation)

    fun getById(reservationId: UUID): Reservation?

    fun getAllByBuyerId(buyerId: UUID): List<Reservation>

    fun getAllBySellerId(sellerId: UUID): List<Reservation>
}