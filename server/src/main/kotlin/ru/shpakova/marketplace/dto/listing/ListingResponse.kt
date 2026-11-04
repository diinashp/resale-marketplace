package ru.shpakova.marketplace.dto.listing

import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import java.math.BigDecimal
import java.util.UUID

class ListingResponse(
    val id: UUID,
    val sellerId: UUID,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val status: ListingStatus,
) {}