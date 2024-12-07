package com.example.slackwss.application.dtos.mapper

import com.example.slackwss.application.dtos.MessageRequest
import com.example.slackwss.application.dtos.MessageResponse
import com.example.slackwss.domains.Message
import java.time.Instant

fun MessageRequest.toDomain(): Message {
    return Message(
        id = Instant.now().toEpochMilli().toString(),
        channelId = channelId,
        text = text,
        senderId = senderId,
        timestamp = Instant.now(),
    )
}

fun Message.toResponse(): MessageResponse {
    return MessageResponse(
        id = this.id,
        channelId = this.channelId,
        text = this.text,
        senderId = this.senderId,
        timeStamp = this.timestamp,
    )
}