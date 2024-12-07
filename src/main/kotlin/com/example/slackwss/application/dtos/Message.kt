package com.example.slackwss.application.dtos

import java.time.Instant


data class MessageRequest(val channelId: String, val text: String, val senderId: String)

data class MessageResponse(
    val id: String,
    val channelId: String,
    val text: String,
    val senderId: String,
    val timeStamp: Instant
)