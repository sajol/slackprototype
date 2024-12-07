package com.example.slackwss.application.services

import com.example.slackwss.domains.Message
import kotlinx.coroutines.reactive.awaitFirstOrNull
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap

@Service
class SlackWebSocketMessageService(
    private val membershipService: MembershipService
) {

    private val logger = KotlinLogging.logger {}
    private val connectionsByUserId = ConcurrentHashMap<String, WebSocketSession>()

    suspend fun addConnection(userId: String, session: WebSocketSession) {
        connectionsByUserId[userId] = session
        membershipService.loadMembershipInfo(userId.toInt())
    }

    fun removeConnection(userId: String) {
        connectionsByUserId.remove(userId)
    }

    suspend fun broadcastMessage(message: Message) {
        val receiverIds = membershipService.getUsersByChannelId(message.channelId.toInt())
        receiverIds?.map { it.toString() }?.forEach { receiverId ->
            val session = connectionsByUserId[receiverId]
            if (session != null && receiverId != message.senderId) {
                session.textMessage("User${message.senderId}: ${message.text}").let {
                    session.sendAndAwait(it)
                }
                logger.info { "Completed broadcasting message to user$receiverId from user${message.senderId}" }
            }
        }
    }
}

// Coroutine extension for sending messages
suspend fun WebSocketSession.sendAndAwait(message: WebSocketMessage) {
    send(Mono.just(message)).awaitFirstOrNull()
}