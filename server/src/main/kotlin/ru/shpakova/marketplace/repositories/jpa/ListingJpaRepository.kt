package ru.shpakova.marketplace.repositories.jpa

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.persistence.jpa.entities.ListingJpaEntity
import java.util.UUID

interface ListingJpaRepository: JpaRepository<ListingJpaEntity, UUID> {
    fun getAllBySellerIdAndStatus(sellerId: UUID, status: ListingStatus): List<ListingJpaEntity>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select listing from ListingJpaEntity listing where listing.id = :id")
    fun findByIdForUpdate(@Param("id") id: UUID): ListingJpaEntity?
}