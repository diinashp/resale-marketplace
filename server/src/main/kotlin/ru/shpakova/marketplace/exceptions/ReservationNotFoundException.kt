package ru.shpakova.marketplace.exceptions

import java.util.UUID

class ReservationNotFoundException(id: UUID):
    RuntimeException("Reservation with id $id not found"){}