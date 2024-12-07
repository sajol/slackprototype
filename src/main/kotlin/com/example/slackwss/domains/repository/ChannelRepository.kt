package com.example.slackwss.domains.repository

import com.example.slackwss.domains.Channel
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ChannelRepository : CoroutineCrudRepository<Channel, Int>