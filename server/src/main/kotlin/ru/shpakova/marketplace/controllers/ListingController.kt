package ru.shpakova.marketplace.controllers

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.shpakova.marketplace.dto.listing.CreateListingRequestDto
import ru.shpakova.marketplace.dto.listing.ListingResponseDto
import ru.shpakova.marketplace.entities.listing.ListingStatus
import ru.shpakova.marketplace.mappers.ListingMapper
import ru.shpakova.marketplace.services.ListingService
import java.util.UUID

@RestController
@RequestMapping("/api/listing")
class ListingController(
    private val listingService: ListingService,
    private val listingMapper: ListingMapper,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createListing(@Valid @RequestBody createListingRequestDto: CreateListingRequestDto) : ListingResponseDto {
        val listing = listingService.createListing(
            createListingRequestDto.sellerId,
            createListingRequestDto.title,
            createListingRequestDto.description,
            createListingRequestDto.price)

       return listingMapper.toResponse(listing)
    }

    @GetMapping("/{listingId}")
    fun getListing(@PathVariable listingId: UUID): ListingResponseDto {
        val listing = listingService.getListing(listingId)
        return listingMapper.toResponse(listing)
    }

    @GetMapping
    fun getSellerListingsByStatus(
        @RequestParam sellerId: UUID,
        @RequestParam status: ListingStatus
    ): List<ListingResponseDto>{
        return listingService.getSellerListingsByStatus(sellerId, status).map{listingMapper.toResponse(it)}
    }

    @DeleteMapping("/{listingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteListing(@RequestParam actorUserId: UUID, @PathVariable listingId: UUID) {
        listingService.deleteListing(actorUserId, listingId)
    }
}