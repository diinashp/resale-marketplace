package ru.shpakova.marketplace.repositories.jpa.adapters

import org.springframework.stereotype.Repository
import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.persistence.jpa.mappers.toDomain
import ru.shpakova.marketplace.persistence.jpa.mappers.toJpaEntity
import ru.shpakova.marketplace.repositories.ReservationRepository
import ru.shpakova.marketplace.repositories.jpa.ReservationJpaRepository
import java.util.UUID

@Repository
class ReservationRepositoryJpaAdapter(
    private val reservationJpaRepository: ReservationJpaRepository,
): ReservationRepository {

    override fun save(reservation: Reservation) {
        val reservationJpaEntity = reservation.toJpaEntity()
        reservationJpaRepository.save(reservationJpaEntity)
    }

    override fun getById(reservationId: UUID): Reservation? {
        val reservationJpaEntity = reservationJpaRepository.findById(reservationId).orElse(null)
        return reservationJpaEntity?.let { it.toDomain() }
    }

    override fun getAllByBuyerId(buyerId: UUID): List<Reservation> {
        return reservationJpaRepository.getAllByBuyerId(buyerId).map{it.toDomain()}
    }

    override fun getAllBySellerId(sellerId: UUID): List<Reservation> {
        return reservationJpaRepository.getAllBySellerId(sellerId).map{it.toDomain()}
    }
}