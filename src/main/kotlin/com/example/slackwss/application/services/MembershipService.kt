package com.example.slackwss.application.services

import com.example.slackwss.domains.repository.MembershipRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArraySet

@Service
class MembershipService(
    private val membershipRepository: MembershipRepository
) {

    val logger = KotlinLogging.logger {}

    private val channelsByUserId = ConcurrentHashMap<Int, CopyOnWriteArraySet<Int>>()
    private val usersByChannelId = ConcurrentHashMap<Int, CopyOnWriteArraySet<Int>>()

    suspend fun loadMembershipInfo(userId: Int) {
        try {
            val channels = membershipRepository.findChannelsByUserId(userId)
            channelsByUserId.computeIfAbsent(userId) { CopyOnWriteArraySet() }.addAll(channels)
            channels.forEach { channelId ->
                usersByChannelId.computeIfAbsent(channelId) { CopyOnWriteArraySet() }.add(userId)
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to load membership" }
        }
    }

    fun getChannelByUserId(userId: Int) = channelsByUserId[userId]

    fun getUsersByChannelId(channelId: Int) = usersByChannelId[channelId]

    fun isMember(senderId: String, channelId: String): Boolean {
        return usersByChannelId[channelId.toInt()]?.contains(senderId.toInt()) ?: false
    }
}