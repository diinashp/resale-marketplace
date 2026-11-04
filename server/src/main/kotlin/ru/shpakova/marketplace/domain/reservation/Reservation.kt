package ru.shpakova.marketplace.domain.reservation

import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import java.util.UUID

data class Reservation(
    val id : UUID = UUID.randomUUID(),
    val listingId : UUID,
    val buyerId : UUID,
    val sellerId : UUID,
    val status: ReservationStatus = ReservationStatus.ACTIVE,
) {
    init{
        require(buyerId != sellerId) { "Seller cannot reserve their own listing" }
    }
}