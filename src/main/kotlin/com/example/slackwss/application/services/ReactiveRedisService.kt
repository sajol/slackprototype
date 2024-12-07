package com.example.slackwss.application.services

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service

@Service
class ReactiveRedisService(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>,
) {
    suspend fun saveValue(key: String, value: String): Boolean {
        // Returns true if the value was successfully set
        return reactiveRedisTemplate.opsForValue().set(key, value).awaitSingle()
    }

    suspend fun getValue(key: String): String? {
        // Returns null if the key doesn't exist
        return reactiveRedisTemplate.opsForValue().get(key).awaitSingleOrNull()
    }

    suspend fun deleteKey(key: String): Long {
        // Returns the number of keys deleted
        return reactiveRedisTemplate.delete(key).awaitSingle()
    }
}