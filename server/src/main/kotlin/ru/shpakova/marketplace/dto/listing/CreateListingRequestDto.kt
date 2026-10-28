package ru.shpakova.marketplace.dto.listing

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.UUID

class CreateListingRequestDto(
    val sellerId: UUID,
    @field:NotBlank(message = "Title cannot be blank")
    @field:Size(max = 50, message = "Title must be at most 50 characters")
    val title: String,
    val description: String,
    @field:DecimalMin(value = "0.00", message = "Price cannot be negative")
    val price: BigDecimal,
) {}