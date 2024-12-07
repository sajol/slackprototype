package com.example.slackwss.application.services

import com.example.slackwss.domains.Message
import com.example.slackwss.domains.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageRepository: MessageRepository,
    private val redisPubSubService: ReactiveRedisPubSubService
) {

    val logger = KotlinLogging.logger {}

    suspend fun save(message: Message): Message {
        return messageRepository.save(message)
    }

    suspend fun getPastMessages(channelId: String, before: Long, limit: Int): List<Message> {
        return messageRepository.findPastMessages(channelId, before, limit).toList()
    }

    suspend fun saveAndPublish(message: Message): Message {
        val savedMessage = messageRepository.save(message)
        logger.info { "Successfully saved message $savedMessage" }
        CoroutineScope(Dispatchers.IO).launch {
            redisPubSubService.publishToChannel(savedMessage)
            logger.info { "Successfully published message $savedMessage" }
        }
        return savedMessage
    }

    suspend fun deleteMessage(messageId: String): Boolean {
        val messageToDelete = messageRepository.findById(messageId) ?: return false

        messageRepository.delete(messageToDelete)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                redisPubSubService.publishToChannel(messageToDelete.copy(text = "[deleted]"))
            } catch (e: Exception) {
                logger.error(e) { "Failed to notify message deletion: ${e.message}" }
            }
        }

        return true
    }
}