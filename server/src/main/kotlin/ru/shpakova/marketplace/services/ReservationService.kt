package ru.shpakova.marketplace.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.shpakova.marketplace.entities.reservation.Reservation
import ru.shpakova.marketplace.exceptions.ListingNotFoundException
import ru.shpakova.marketplace.exceptions.ReservationNotFoundException
import ru.shpakova.marketplace.repositories.ListingRepository
import ru.shpakova.marketplace.repositories.ReservationRepository
import java.util.UUID


@Service
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val listingRepository: ListingRepository,
) {
    private val logger = LoggerFactory.getLogger(ReservationService::class.java)

    @Transactional
    fun placeReservation(actorUserId: UUID, listingId: UUID): Reservation {
        val listing = listingRepository.getByIdForUpdate(listingId) ?: throw ListingNotFoundException(listingId)
        val reservation = Reservation(UUID.randomUUID(), listingId, actorUserId, listing.sellerId)

        listing.reserve()

        listingRepository.save(listing)
        reservationRepository.save(reservation)
        logger.info(
            "Reservation placed: reservationId={}, listingId={}, buyerId={}",
            reservation.id,
            listingId,
            actorUserId,
        )

        return reservation
    }

    @Transactional
    fun cancelReservation(actorUserId: UUID, reservationId: UUID){
        val reservation = reservationRepository.getById(reservationId) ?: throw ReservationNotFoundException(reservationId)
        val listing = listingRepository.getById(reservation.listingId) ?: throw ListingNotFoundException(reservation.listingId)

        require(actorUserId == reservation.sellerId || actorUserId == reservation.buyerId) {"You cannot cancel reservation"}

        listing.releaseReservation()
        reservation.cancel()

        listingRepository.save(listing)
        reservationRepository.save(reservation)
        logger.info(
            "Reservation cancelled: reservationId={}, listingId={}, actorUserId={}",
            reservationId,
            reservation.listingId,
            actorUserId,
        )
    }

    @Transactional
    fun confirmReceipt(actorUserId: UUID, reservationId: UUID){
        val reservation = reservationRepository.getById(reservationId) ?: throw ReservationNotFoundException(reservationId)
        val listing = listingRepository.getById(reservation.listingId) ?: throw ListingNotFoundException(reservation.listingId)

        require(actorUserId == reservation.buyerId) {"You cannot confirm receipt"}

        listing.markAsSold()
        reservation.complete()

        listingRepository.save(listing)
        reservationRepository.save(reservation)
        logger.info(
            "Receipt confirmed: reservationId={}, listingId={}, buyerId={}",
            reservationId,
            reservation.listingId,
            actorUserId,
        )
    }

    fun getReservation(reservationId: UUID): Reservation {
        return reservationRepository.getById(reservationId) ?: throw ReservationNotFoundException(reservationId)
    }

    fun getBuyerReservations(buyerId: UUID): List<Reservation> {
        return reservationRepository.getAllByBuyerId(buyerId)
    }

    fun getSellerReservations(sellerId: UUID): List<Reservation> {
        return reservationRepository.getAllBySellerId(sellerId)

    }
}
