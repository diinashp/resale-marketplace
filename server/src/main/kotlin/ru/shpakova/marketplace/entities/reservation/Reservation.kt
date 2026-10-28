package ru.shpakova.marketplace.entities.reservation

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Reservation(
    @field:Id
    val id : UUID = UUID.randomUUID(),
    val listingId : UUID,
    val buyerId : UUID,
    val sellerId : UUID,
){
    @field:Enumerated(EnumType.STRING)
    var status: ReservationStatus = ReservationStatus.ACTIVE

    init{
        require(buyerId != sellerId) { "Seller cannot reserve their own listing" }
    }

    fun cancel(){
        check (status == ReservationStatus.ACTIVE){"Reservation is not active"}
        status = ReservationStatus.CANCELLED
    }

    fun complete(){
        check (status == ReservationStatus.ACTIVE){"Reservation is not active"}
        status = ReservationStatus.COMPLETED
    }
}