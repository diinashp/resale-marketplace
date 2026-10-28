package ru.shpakova.marketplace.entities.listing

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.math.BigDecimal
import java.util.UUID

@Entity
class Listing(
    @field:Id
    val id: UUID = UUID.randomUUID(),
    val sellerId: UUID,
    val title: String,
    val description: String,
    val price: BigDecimal,
){
    @field:Enumerated(EnumType.STRING)
    var status: ListingStatus = ListingStatus.AVAILABLE

    init{
        require(title.isNotBlank()){"Title cannot be blank"}
        require(price >= BigDecimal.ZERO){"Price cannot be negative"}
    }

    fun reserve(){
        check(status == ListingStatus.AVAILABLE){"Listing is not available"}
        status = ListingStatus.RESERVED
    }

    fun releaseReservation(){
        check(status == ListingStatus.RESERVED){"Listing is not reserved"}
        status = ListingStatus.AVAILABLE
    }

    fun markAsSold(){
        check(status == ListingStatus.RESERVED){"Listing is not reserved"}
        status = ListingStatus.SOLD
    }
}