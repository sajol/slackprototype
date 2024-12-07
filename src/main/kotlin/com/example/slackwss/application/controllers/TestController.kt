package com.example.slackwss.application.controllers

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(private val connectionFactory: ConnectionFactory) {

    @GetMapping("/test")
    suspend fun test(): String {
        return connectionFactory.create().awaitSingle().toString()
    }
}