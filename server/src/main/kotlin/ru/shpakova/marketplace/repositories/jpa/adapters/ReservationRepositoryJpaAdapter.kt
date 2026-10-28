package ru.shpakova.marketplace.repositories.jpa.adapters

import org.springframework.stereotype.Repository
import ru.shpakova.marketplace.entities.reservation.Reservation
import ru.shpakova.marketplace.repositories.ReservationRepository
import ru.shpakova.marketplace.repositories.jpa.ReservationJpaRepository
import java.util.UUID

@Repository
class ReservationRepositoryJpaAdapter(
    private val reservationJpaRepository: ReservationJpaRepository
): ReservationRepository {
    override fun save(reservation: Reservation) {
        reservationJpaRepository.save(reservation)
    }

    override fun getById(reservationId: UUID): Reservation? {
        return reservationJpaRepository.findById(reservationId).orElse(null)
    }

    override fun getAllByBuyerId(buyerId: UUID): List<Reservation> {
        return reservationJpaRepository.getAllByBuyerId(buyerId)
    }

    override fun getAllBySellerId(sellerId: UUID): List<Reservation> {
        return reservationJpaRepository.getAllBySellerId(sellerId)
    }
}