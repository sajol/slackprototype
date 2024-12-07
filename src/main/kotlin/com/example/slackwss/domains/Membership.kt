package com.example.slackwss.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("memberships")
data class Membership(
    @Id val id: Int? = null,
    val userId: Int,
    val channelId: Int
)