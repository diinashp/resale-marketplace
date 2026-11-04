package ru.shpakova.marketplace.persistence.jpa.entities

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "listing")
class ListingJpaEntity(
    @field:Id
    var id: UUID = UUID.randomUUID(),
    var sellerId: UUID,
    var title: String,
    var description: String,
    var price: BigDecimal,
    @field:Enumerated(EnumType.STRING)
    var status: ListingStatus,
) {}