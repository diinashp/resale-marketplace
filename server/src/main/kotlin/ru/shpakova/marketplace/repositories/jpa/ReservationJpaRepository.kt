package ru.shpakova.marketplace.repositories.jpa

import org.springframework.data.jpa.repository.JpaRepository
import ru.shpakova.marketplace.persistence.jpa.entities.ReservationJpaEntity
import java.util.UUID

interface ReservationJpaRepository: JpaRepository<ReservationJpaEntity, UUID> {
    fun getAllByBuyerId(buyerId: UUID): List<ReservationJpaEntity>

    fun getAllBySellerId(sellerId: UUID): List<ReservationJpaEntity>
}