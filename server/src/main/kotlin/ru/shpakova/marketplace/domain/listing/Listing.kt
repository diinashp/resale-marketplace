package ru.shpakova.marketplace.domain.listing

import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import java.math.BigDecimal
import java.util.UUID
import kotlin.text.compareTo

data class Listing (
    val id: UUID = UUID.randomUUID(),
    val sellerId: UUID,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val status: ListingStatus = ListingStatus.AVAILABLE,
){
    init{
        require(title.isNotBlank()){"Title cannot be blank"}
        require(price >= BigDecimal.ZERO){"Price cannot be negative"}
    }
}