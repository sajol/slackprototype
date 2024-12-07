package com.example.slackwss.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("channels")
data class Channel(
    @Id
    val id: Int? = null,
    val name: String,
    val type: String
)