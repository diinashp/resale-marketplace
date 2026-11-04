package ru.shpakova.marketplace.domain.listing.states

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException

class ListingReservedState(): ListingState {
    override fun listingStatus(): ListingStatus {
        return ListingStatus.RESERVED
    }

    override fun reserveListing(listing: Listing): Listing{
        throw InvalidStateTransitionException("Cannot reserve listing: it is already reserved")
    }

    override fun releaseReservation(listing: Listing) : Listing {
        return listing.copy(status = ListingStatus.AVAILABLE)
    }

    override fun markAsSold(listing: Listing): Listing {
        return listing.copy(status = ListingStatus.SOLD)
    }
}
