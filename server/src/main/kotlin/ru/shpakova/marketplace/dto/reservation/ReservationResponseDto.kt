package ru.shpakova.marketplace.dto.reservation

import ru.shpakova.marketplace.entities.reservation.ReservationStatus
import java.util.UUID

class ReservationResponseDto(
    val id: UUID,
    val listingId: UUID,
    val buyerId: UUID,
    val sellerId: UUID,
    val status: ReservationStatus,
) {}