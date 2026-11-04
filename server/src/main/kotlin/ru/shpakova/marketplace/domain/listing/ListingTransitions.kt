package ru.shpakova.marketplace.domain.listing

import ru.shpakova.marketplace.domain.listing.enums.ListingStatus
import ru.shpakova.marketplace.domain.listing.states.ListingAvailableState
import ru.shpakova.marketplace.domain.listing.states.ListingReservedState
import ru.shpakova.marketplace.domain.listing.states.ListingSoldState
import ru.shpakova.marketplace.domain.listing.states.ListingState

private fun Listing.currentState(): ListingState {
    return when (status) {
        ListingStatus.AVAILABLE -> ListingAvailableState()
        ListingStatus.RESERVED -> ListingReservedState()
        ListingStatus.SOLD -> ListingSoldState()
    }
}

fun Listing.reserveListing() : Listing {
    return currentState().reserveListing(this)
}

fun Listing.releaseReservation(): Listing {
    return currentState().releaseReservation(this)
}

fun Listing.markAsSold(): Listing {
    return currentState().markAsSold(this)
}