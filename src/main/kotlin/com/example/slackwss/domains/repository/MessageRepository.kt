package com.example.slackwss.domains.repository

import com.example.slackwss.domains.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun findById(id: String): Message?
    suspend fun save(message: Message): Message
    suspend fun delete(message: Message)
    suspend fun findPastMessages(
        channelId: String,
        before: Long, //Timestamp to filter messages before this time
        limit: Int,
    ): Flow<Message>
}