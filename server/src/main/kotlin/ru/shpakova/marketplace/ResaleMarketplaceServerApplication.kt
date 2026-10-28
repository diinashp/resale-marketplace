package ru.shpakova.marketplace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ResaleMarketplaceServerApplication

fun main(args: Array<String>) {
	runApplication<ResaleMarketplaceServerApplication>(*args)
}
