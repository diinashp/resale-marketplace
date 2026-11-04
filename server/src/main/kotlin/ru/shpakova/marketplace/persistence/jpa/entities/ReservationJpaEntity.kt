package ru.shpakova.marketplace.persistence.jpa.entities

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.shpakova.marketplace.domain.reservation.enums.ReservationStatus
import java.util.UUID

@Entity
@Table(name = "reservation")
class ReservationJpaEntity(
    @field:Id
    var id : UUID = UUID.randomUUID(),
    var listingId : UUID,
    var buyerId : UUID,
    var sellerId : UUID,
    @field:Enumerated(EnumType.STRING)
    var status: ReservationStatus,
) {}