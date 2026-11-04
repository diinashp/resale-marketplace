package ru.shpakova.marketplace.domain.listing.states

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus

interface ListingState {
    fun listingStatus(): ListingStatus

    fun reserveListing(listing: Listing): Listing

    fun releaseReservation(listing: Listing): Listing

    fun markAsSold(listing: Listing): Listing
}