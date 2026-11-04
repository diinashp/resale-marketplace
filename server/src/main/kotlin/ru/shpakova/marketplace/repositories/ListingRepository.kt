package ru.shpakova.marketplace.repositories

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import java.util.UUID

interface ListingRepository {

    fun save(listing: Listing)

    fun getById(id: UUID): Listing?

    fun getAllBySellerIdAndStatus(sellerId: UUID, status: ListingStatus): List<Listing>

    fun deleteById(listingId: UUID)

    fun getByIdForUpdate(id: UUID): Listing?
}