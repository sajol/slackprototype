package com.example.slackwss.domains.repository

import com.example.slackwss.domains.Membership
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MembershipRepository : CoroutineCrudRepository<Membership, Int> {

    @Query("SELECT channel_id FROM memberships WHERE user_id = :userId")
    suspend fun findChannelsByUserId(userId: Int): List<Int>
}