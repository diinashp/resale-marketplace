package ru.shpakova.marketplace.mappers

import org.springframework.stereotype.Component
import ru.shpakova.marketplace.dto.listing.ListingResponseDto
import ru.shpakova.marketplace.entities.listing.Listing

@Component
class ListingMapper {
    fun toResponse(listing: Listing) =
        ListingResponseDto(listing.id, listing.sellerId, listing.title, listing.description, listing.price, listing.status)
}