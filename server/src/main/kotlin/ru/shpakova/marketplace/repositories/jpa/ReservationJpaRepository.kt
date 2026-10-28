package ru.shpakova.marketplace.repositories.jpa

import org.springframework.data.jpa.repository.JpaRepository
import ru.shpakova.marketplace.entities.reservation.Reservation
import java.util.UUID

interface ReservationJpaRepository: JpaRepository<Reservation, UUID> {
    fun getAllByBuyerId(buyerId: UUID): List<Reservation>

    fun getAllBySellerId(sellerId: UUID): List<Reservation>
}