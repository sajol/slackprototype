package com.example.slackwss.domains.repository

import com.example.slackwss.domains.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, Int>