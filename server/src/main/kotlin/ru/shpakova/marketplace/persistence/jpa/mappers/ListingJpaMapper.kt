package ru.shpakova.marketplace.persistence.jpa.mappers

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.persistence.jpa.entities.ListingJpaEntity

fun Listing.toJpaEntity(): ListingJpaEntity =
    ListingJpaEntity(
        id = id,
        sellerId = sellerId,
        title = title,
        description = description,
        price = price,
        status = status
    )

fun ListingJpaEntity.toDomain(): Listing =
     Listing(
        id = id,
        sellerId = sellerId,
        title = title,
        description = description,
        price = price,
        status = status
    )