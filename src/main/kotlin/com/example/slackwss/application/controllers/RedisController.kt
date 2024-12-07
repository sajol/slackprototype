package com.example.slackwss.application.controllers

import com.example.slackwss.application.services.ReactiveRedisService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/redis")
class RedisController(
    private val reactiveRedisService: ReactiveRedisService
) {

    @PostMapping
    suspend fun save(@RequestParam key: String, @RequestParam value: String): String {
        val success = reactiveRedisService.saveValue(key, value)
        return if (success) "Saved key=$key, value=$value" else "Failed to save key=$key"
    }

    @GetMapping
    suspend fun get(@RequestParam key: String): String? {
        val value = reactiveRedisService.getValue(key)
        return value ?: "Key not found: $key"
    }

    @DeleteMapping
    suspend fun delete(@RequestParam key: String): String {
        val deleteCount = reactiveRedisService.deleteKey(key)
        return if (deleteCount > 0) "Deleted $key: $deleteCount" else "Key not found: $key"
    }
}