package com.example.slackwss.configs

import com.example.slackwss.application.handlers.SlackWebSocketHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

data class Message(val senderId: String, val text: String, val channelId: String)

@Configuration
class SlackWebSocketConfig {

    @Bean
    fun handlerMapping(slackWebSocketHandler: SlackWebSocketHandler): HandlerMapping {
        val map = mapOf("/ws" to slackWebSocketHandler)
        return SimpleUrlHandlerMapping(map, 1)
    }

    @Bean
    fun webSocketHandlerAdapter(): WebSocketHandlerAdapter {
        return WebSocketHandlerAdapter()
    }
}