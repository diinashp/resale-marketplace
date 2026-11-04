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
import ru.shpakova.marketplace.dto.listing.CreateListingRequest
import ru.shpakova.marketplace.dto.listing.ListingResponse
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.mappers.toResponse
import ru.shpakova.marketplace.services.ListingService
import java.util.UUID

@RestController
@RequestMapping("/api/listing")
class ListingController(
    private val listingService: ListingService,
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createListing(@Valid @RequestBody createListingRequestDto: CreateListingRequest) : ListingResponse {
        val listing = listingService.createListing(
            createListingRequestDto.sellerId,
            createListingRequestDto.title,
            createListingRequestDto.description,
            createListingRequestDto.price)

       return listing.toResponse()
    }

    @GetMapping("/{listingId}")
    fun getListing(@PathVariable listingId: UUID): ListingResponse {
        val listing = listingService.getListing(listingId)
        return listing.toResponse()
    }

    @GetMapping
    fun getSellerListingsByStatus(
        @RequestParam sellerId: UUID,
        @RequestParam status: ListingStatus
    ): List<ListingResponse>{
        return listingService.getSellerListingsByStatus(sellerId, status).map{it.toResponse()}
    }

    @DeleteMapping("/{listingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteListing(@RequestParam actorUserId: UUID, @PathVariable listingId: UUID) {
        listingService.deleteListing(actorUserId, listingId)
    }
}