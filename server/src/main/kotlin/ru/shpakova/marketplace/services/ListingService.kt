package ru.shpakova.marketplace.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.shpakova.marketplace.entities.listing.Listing
import ru.shpakova.marketplace.entities.listing.ListingStatus
import ru.shpakova.marketplace.exceptions.ListingNotFoundException
import ru.shpakova.marketplace.repositories.ListingRepository
import java.math.BigDecimal
import java.util.UUID

@Service
class ListingService(
    private val listingRepository: ListingRepository
) {
    private val logger = LoggerFactory.getLogger(ListingService::class.java)

    fun createListing(actorUserId: UUID, title: String, description: String, price: BigDecimal): Listing {
        val listing = Listing(UUID.randomUUID(), actorUserId, title, description, price)
        listingRepository.save(listing)
        logger.info("Listing created: listingId={}, sellerId={}", listing.id, actorUserId)

        return listing
    }

    fun getListing(listingId: UUID): Listing {
        return listingRepository.getById(listingId) ?: throw ListingNotFoundException(listingId)
    }

    fun getSellerListingsByStatus(sellerId: UUID, status: ListingStatus): List<Listing> {
        return listingRepository.getAllBySellerIdAndStatus(sellerId, status)
    }

    fun deleteListing(actorUserId: UUID, listingId: UUID) {
        val listing = listingRepository.getById(listingId) ?: throw ListingNotFoundException(listingId)
        require(listing.sellerId == actorUserId){"Seller can only delete their own listings"}
        check(listing.status == ListingStatus.AVAILABLE){"Listing is not available to delete"}
        listingRepository.deleteById(listingId)
        logger.info("Listing deleted: listingId={}, sellerId={}", listingId, actorUserId)
    }
}
