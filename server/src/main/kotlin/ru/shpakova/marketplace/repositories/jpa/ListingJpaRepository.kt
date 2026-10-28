package ru.shpakova.marketplace.repositories.jpa

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.shpakova.marketplace.entities.listing.Listing
import ru.shpakova.marketplace.entities.listing.ListingStatus
import java.util.UUID

interface ListingJpaRepository: JpaRepository<Listing, UUID> {
    fun getAllBySellerIdAndStatus(sellerId: UUID, status: ListingStatus): List<Listing>

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select listing from Listing listing where listing.id = :id")
    fun findByIdForUpdate(@Param("id") id: UUID): Listing?
}