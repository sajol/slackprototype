package com.example.slackwss.application.controllers

import com.example.slackwss.domains.User
import com.example.slackwss.domains.repository.UserRepository
import kotlinx.coroutines.flow.toList
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userRepository: UserRepository,
) {

    @GetMapping("/users")
    suspend fun getUsers(): List<User> {
        return userRepository.findAll().toList()
    }
}