package ru.shpakova.marketplace.mappers

import org.springframework.stereotype.Component
import ru.shpakova.marketplace.dto.reservation.ReservationResponseDto
import ru.shpakova.marketplace.entities.reservation.Reservation

@Component
class ReservationMapper {
    fun toResponse(reservation: Reservation) =
        ReservationResponseDto(reservation.id, reservation.listingId, reservation.buyerId, reservation.sellerId, reservation.status)
}