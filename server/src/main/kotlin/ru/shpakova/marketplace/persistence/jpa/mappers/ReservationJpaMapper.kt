package ru.shpakova.marketplace.persistence.jpa.mappers

import ru.shpakova.marketplace.domain.reservation.Reservation
import ru.shpakova.marketplace.persistence.jpa.entities.ReservationJpaEntity


fun Reservation.toJpaEntity(): ReservationJpaEntity =
    ReservationJpaEntity(
        id = id,
        listingId = listingId,
        buyerId = buyerId,
        sellerId = sellerId,
        status = status
    )


fun ReservationJpaEntity.toDomain(): Reservation =
     Reservation(
        id = id,
        listingId = listingId,
        buyerId = buyerId,
        sellerId = sellerId,
        status = status
    )
