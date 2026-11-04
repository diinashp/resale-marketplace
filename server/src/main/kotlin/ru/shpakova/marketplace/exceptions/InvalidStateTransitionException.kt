package ru.shpakova.marketplace.exceptions

class InvalidStateTransitionException(message: String):
    RuntimeException(message){}