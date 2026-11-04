package ru.shpakova.marketplace.domain.listing.states

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException

class ListingSoldState(): ListingState {
    override fun listingStatus(): ListingStatus {
        return ListingStatus.SOLD
    }

    override fun reserveListing(listing: Listing): Listing{
        throw InvalidStateTransitionException("Cannot reserve listing: it has already been sold")
    }

    override fun releaseReservation(listing: Listing): Listing {
        throw InvalidStateTransitionException("Cannot release reservation: listing has already been sold")
    }

    override fun markAsSold(listing: Listing): Listing {
        throw InvalidStateTransitionException("Cannot mark listing as sold: it has already been sold")
    }
}
