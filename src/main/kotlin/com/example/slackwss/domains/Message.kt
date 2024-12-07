package com.example.slackwss.domains

import com.example.slackwss.application.common.InstantSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Serializable
@Document(collection = "messages")
data class Message(
    @Id
    val id: String = Instant.now().toEpochMilli().toString(),
    @Indexed
    val channelId: String,
    val text: String,
    val senderId: String,
    @Serializable(with = InstantSerializer::class)
    val timestamp: Instant = Instant.now(),
)

fun Message.toJsonString(): String = Json.encodeToString(Message.serializer(), this)