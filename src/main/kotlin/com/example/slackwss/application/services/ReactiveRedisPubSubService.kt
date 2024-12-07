package com.example.slackwss.application.services

import com.example.slackwss.domains.Message
import com.example.slackwss.domains.toJsonString
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

@Service
class ReactiveRedisPubSubService(
    reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory,
    private val reactiveRedisMessageListenerContainer: ReactiveRedisMessageListenerContainer,
    private val webSocketMessageService: SlackWebSocketMessageService,
    private val membershipService: MembershipService,
) {

    private val logger = KotlinLogging.logger {}
    private val pubSubCommands = reactiveRedisConnectionFactory.reactiveConnection.pubSubCommands()
    private val activeChannelSubscriptions = ConcurrentHashMap.newKeySet<Int>()

    suspend fun subscribeToChannel(userId: String) {
        val channelIds = membershipService.getChannelByUserId(userId.toInt())
        channelIds?.forEach { channelId ->
            try {
                if (activeChannelSubscriptions.add(channelId)) {
                    logger.info("Subscribed to channelId=$channelId")
                    reactiveRedisMessageListenerContainer.receive(ChannelTopic(channelId.toString()))
                        .asFlow()
                        .map { it.toMessage() }
                        .collect(webSocketMessageService::broadcastMessage)
                }
            } catch (e: Exception) {
                logger.error(e) { "Failed to subscribe to channel: $channelId, Error: ${e.message}" }
            }
        }
    }

    suspend fun publishToChannel(message: Message): Long {
        return try {
            pubSubCommands
                .publish(message.channelId.toByteBuffer(), message.toJsonString().toByteBuffer())
                .awaitSingle()
        } catch (e: Exception) {
            logger.error(e) { "Failed to publish message to channel: ${message.channelId}, Error: ${e.message}" }
            0L
        }
    }
}

fun String.toByteBuffer(): ByteBuffer {
    return StandardCharsets.UTF_8.encode(this)
}

fun ReactiveSubscription.Message<String, String>.toMessage(): Message {
    val payload = message.toString()
    return Json.decodeFromString(Message.serializer(), payload)
}