package com.example.slackwss.infrastructure.peristence

import com.example.slackwss.domains.Message
import com.example.slackwss.domains.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class MongoMessageRepository(
    private val reactiveMongoTemplate: ReactiveMongoTemplate,
) : MessageRepository {
    override suspend fun findById(id: String): Message? {
        val query = Query.query(Criteria.where("id").`is`(id))
        return reactiveMongoTemplate.findOne(query, Message::class.java).awaitSingleOrNull()
    }

    override suspend fun save(message: Message): Message {
        return reactiveMongoTemplate.save(message).awaitSingle()
    }

    override suspend fun delete(message: Message) {
        reactiveMongoTemplate.remove(message).awaitSingle()
    }

    override suspend fun findPastMessages(channelId: String, before: Long, limit: Int): Flow<Message> {
        val query = Query().apply {
            addCriteria(Criteria.where("channelId").`is`(channelId))
            addCriteria(Criteria.where("timestamp").lte(before))
            limit(limit)
            Sort.by(Sort.Direction.DESC, "timestamp")
        }
        return reactiveMongoTemplate.find(query, Message::class.java).asFlow()
    }
}