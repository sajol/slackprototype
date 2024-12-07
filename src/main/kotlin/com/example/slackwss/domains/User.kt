package com.example.slackwss.domains

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("users")
data class User(
    @Id
    val id: Int? = null,
    val name: String? = null
)