package ru.shpakova.marketplace.dto.reservation

import java.util.UUID

class CreateReservationRequestDto(
    val listingId: UUID,
    val buyerId: UUID,
) {}