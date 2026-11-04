package ru.shpakova.marketplace.repositories.jpa.adapters

import org.springframework.stereotype.Repository
import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.persistence.jpa.mappers.toDomain
import ru.shpakova.marketplace.persistence.jpa.mappers.toJpaEntity
import ru.shpakova.marketplace.repositories.ListingRepository
import ru.shpakova.marketplace.repositories.jpa.ListingJpaRepository
import java.util.UUID

@Repository
class ListingRepositoryJpaAdapter(
    private val listingJpaRepository: ListingJpaRepository,
): ListingRepository{

    override fun save(listing: Listing) {
        val listingJpaEntity = listing.toJpaEntity()
        listingJpaRepository.save(listingJpaEntity)
    }

    override fun getById(id: UUID): Listing? {
        val listingJpaEntity = listingJpaRepository.findById(id).orElse(null)
        return listingJpaEntity?.let { listingJpaEntity.toDomain() }
    }

    override fun getAllBySellerIdAndStatus(sellerId: UUID, status: ListingStatus): List<Listing> {
        return listingJpaRepository.getAllBySellerIdAndStatus(sellerId, status).map{it.toDomain()}
    }

    override fun deleteById(listingId: UUID) {
        listingJpaRepository.deleteById(listingId)
    }

    override fun getByIdForUpdate(id: UUID): Listing? {
        val listingJpaEntity = listingJpaRepository.findByIdForUpdate(id) ?: return null
        return listingJpaEntity.toDomain()
    }
}