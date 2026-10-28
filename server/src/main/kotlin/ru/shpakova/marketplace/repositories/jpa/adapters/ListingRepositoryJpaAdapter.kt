package ru.shpakova.marketplace.repositories.jpa.adapters

import org.springframework.stereotype.Repository
import ru.shpakova.marketplace.entities.listing.Listing
import ru.shpakova.marketplace.entities.listing.ListingStatus
import ru.shpakova.marketplace.repositories.ListingRepository
import ru.shpakova.marketplace.repositories.jpa.ListingJpaRepository
import java.util.UUID

@Repository
class ListingRepositoryJpaAdapter(
    private val listingJpaRepository: ListingJpaRepository
): ListingRepository{

    override fun save(listing: Listing) {
        listingJpaRepository.save(listing)
    }

    override fun getById(id: UUID): Listing? {
        return listingJpaRepository.findById(id).orElse(null)
    }

    override fun getAllBySellerIdAndStatus(sellerId: UUID, status: ListingStatus): List<Listing> {
        return listingJpaRepository.getAllBySellerIdAndStatus(sellerId, status)
    }

    override fun deleteById(listingId: UUID) {
        listingJpaRepository.deleteById(listingId)
    }

    override fun getByIdForUpdate(id: UUID): Listing? {
        return listingJpaRepository.findByIdForUpdate(id)
    }
}