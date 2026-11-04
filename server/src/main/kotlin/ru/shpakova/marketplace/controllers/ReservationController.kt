package ru.shpakova.marketplace.controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.shpakova.marketplace.dto.reservation.CreateReservationRequest
import ru.shpakova.marketplace.dto.reservation.ReservationResponse
import ru.shpakova.marketplace.mappers.toResponse
import ru.shpakova.marketplace.services.ReservationService
import java.util.UUID

@RestController
@RequestMapping("/api/reservations")
class ReservationController(
    private val reservationService: ReservationService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun placeReservation(@RequestBody createReservationRequestDto: CreateReservationRequest): ReservationResponse {
        val reservation = reservationService.placeReservation(
            createReservationRequestDto.buyerId,
            createReservationRequestDto.listingId)
        return reservation.toResponse()
    }

    @PostMapping("/{reservationId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun cancelReservation(@RequestParam actorUserId: UUID, @PathVariable reservationId: UUID) {
        reservationService.cancelReservation(actorUserId, reservationId)
    }

    @PostMapping("/{reservationId}/confirm-receipt")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirmReceipt(@RequestParam actorUserId: UUID, @PathVariable reservationId: UUID) {
        reservationService.confirmReceipt(actorUserId, reservationId)

    }

    @GetMapping("/{reservationId}")
    fun getReservation(@PathVariable reservationId: UUID): ReservationResponse {
        val reservation = reservationService.getReservation(reservationId)
        return reservation.toResponse()
    }

    @GetMapping("/buyer")
    fun getBuyerReservations(@RequestParam buyerId: UUID): List<ReservationResponse> {
        return reservationService.getBuyerReservations(buyerId).map { it.toResponse() }
    }

    @GetMapping("/seller")
    fun getSellerReservations(@RequestParam sellerId: UUID): List<ReservationResponse> {
        return reservationService.getSellerReservations(sellerId).map { it.toResponse() }
    }
}