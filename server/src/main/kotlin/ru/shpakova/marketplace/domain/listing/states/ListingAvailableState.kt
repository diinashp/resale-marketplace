package ru.shpakova.marketplace.domain.listing.states

import ru.shpakova.marketplace.domain.listing.Listing
import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.exceptions.InvalidStateTransitionException

class ListingAvailableState(): ListingState {

    override fun listingStatus(): ListingStatus {
        return ListingStatus.AVAILABLE
    }

    override fun reserveListing(listing: Listing): Listing {
        return listing.copy(status = ListingStatus.RESERVED)
    }

    override fun releaseReservation(listing: Listing): Listing {
        throw InvalidStateTransitionException("Cannot release reservation: listing is not reserved")
    }

    override fun markAsSold(listing: Listing): Listing {
        throw InvalidStateTransitionException("Cannot mark listing as sold: it has no active reservation")
    }
}
