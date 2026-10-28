package ru.shpakova.marketplace.exceptions

import java.util.UUID

class ListingNotFoundException(id: UUID):
    RuntimeException("Listing with id $id not found"){}