package ru.shpakova.marketplace.mappers

import ru.shpakova.marketplace.dto.listing.ListingResponse
import ru.shpakova.marketplace.domain.listing.Listing

fun Listing.toResponse() =
    ListingResponse(
        id = id,
        sellerId = sellerId,
        title = title,
        description = description,
        price = price,
        status = status
    )