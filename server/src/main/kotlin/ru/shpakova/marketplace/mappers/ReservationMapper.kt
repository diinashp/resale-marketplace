package ru.shpakova.marketplace.mappers

import ru.shpakova.marketplace.dto.reservation.ReservationResponse
import ru.shpakova.marketplace.domain.reservation.Reservation

fun Reservation.toResponse() = ReservationResponse(
    id = id,
    listingId = listingId,
    buyerId = buyerId,
    sellerId = sellerId,
    status = status,
)