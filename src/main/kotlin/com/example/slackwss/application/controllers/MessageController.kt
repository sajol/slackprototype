package com.example.slackwss.application.controllers

import com.example.slackwss.application.dtos.MessageRequest
import com.example.slackwss.application.dtos.MessageResponse
import com.example.slackwss.application.dtos.mapper.toDomain
import com.example.slackwss.application.dtos.mapper.toResponse
import com.example.slackwss.application.services.MembershipService
import com.example.slackwss.application.services.MessageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class MessageController(
    private val membershipService: MembershipService,
    private val messageService: MessageService,
) {

    @PostMapping("/messages")
    suspend fun sendMessage(@RequestBody messageRequest: MessageRequest): ResponseEntity<String> {
        val senderId = messageRequest.senderId
        val channelId = messageRequest.channelId

        if (!membershipService.isMember(messageRequest.senderId, messageRequest.channelId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User $senderId is not a member of $channelId")
        }

        messageService.saveAndPublish(messageRequest.toDomain())
        return ResponseEntity.ok("Message sent.")
    }

    @PostMapping("v1/messages")
    suspend fun saveMessage(@RequestBody messageRequest: MessageRequest): MessageResponse {
        val message = messageService.save(messageRequest.toDomain())
        return message.toResponse()
    }

    @DeleteMapping("v1/messages/{id}")
    suspend fun deleteMessage(@PathVariable id: String): Boolean {
        return messageService.deleteMessage(id)
    }


}