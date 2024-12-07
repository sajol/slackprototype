package com.example.slackwss.application.handlers

import com.example.slackwss.application.services.ReactiveRedisPubSubService
import com.example.slackwss.application.services.SlackWebSocketMessageService
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@Component
class SlackWebSocketHandler(
    private val webSocketMessageService: SlackWebSocketMessageService,
    private val reactiveRedisPubSubService: ReactiveRedisPubSubService,
) : WebSocketHandler {
    private val logger = KotlinLogging.logger {}

    override fun handle(session: WebSocketSession): Mono<Void> {
        return mono {
            val queryParams = session.handshakeInfo.uri.query.split("&").associate {
                val parts = it.split("=")
                parts[0] to parts.getOrNull(1)
            }

            val userId = queryParams["userId"]

            if (userId == null) {
                logger.error("Missing required parameters: userId")
                // Close the WebSocket connection with an error status
                session.close(CloseStatus(1008, "Missing required parameters: userId"))
                    .awaitSingleOrNull()
                return@mono
            }

            webSocketMessageService.addConnection(userId, session)
            logger.info("Connection added for userId=$userId")

            reactiveRedisPubSubService.subscribeToChannel(userId)

            session.receive()
                .doFinally {
                    // Cleanup connection when the client explicitly closes
                    webSocketMessageService.removeConnection(userId)
                    logger.info("Connection closed by client for userId=$userId")
                }
                .then()
                .awaitSingleOrNull()
        }.then()
    }

}