package ru.shpakova.marketplace.dto.reservation

import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import java.util.UUID

class ReservationResponse(
    val id: UUID,
    val listingId: UUID,
    val buyerId: UUID,
    val sellerId: UUID,
    val status: ReservationStatus,
) {}