package com.example.slackwss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SlackwssApplication

fun main(args: Array<String>) {
	runApplication<SlackwssApplication>(*args)
}
