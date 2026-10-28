package ru.shpakova.marketplace.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = [ListingNotFoundException::class, ReservationNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(exception: RuntimeException): Map<String, Any>{
        return mapOf(
            "status" to 404,
            "error" to "NOT_FOUND",
            "message" to (exception.message ?: "Resource not found"),
        )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidArgument(exception: IllegalArgumentException): Map<String, Any>{
        return mapOf(
            "status" to 400,
            "error" to "BAD_REQUEST",
            "message" to (exception.message ?: "Invalid request"),
        )
    }

    @ExceptionHandler(IllegalStateException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleInvalidState(exception: IllegalStateException): Map<String, Any>{
        return mapOf(
            "status" to 409,
            "error" to "CONFLICT",
            "message" to (exception.message ?: "Operation conflicts with the current state"),
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidation(exception: MethodArgumentNotValidException) : Map<String, Any>{
        val message = exception.bindingResult.fieldErrors
            .joinToString("; "){fieldError -> "${fieldError.field}: ${fieldError.defaultMessage ?: "Invalid value"}"}
            .ifBlank { "Request validation failed" }

        return mapOf(
            "status" to 400,
            "error" to "BAD_REQUEST",
            "message" to message,
        )
    }
}