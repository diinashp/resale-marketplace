package ru.shpakova.marketplace.dto.reservation

import java.util.UUID

class CreateReservationRequest(
    val listingId: UUID,
    val buyerId: UUID,
)