package com.funcorp.bandit

import com.funcorp.bandit.content.repository.ContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
class BanditApplication @Autowired constructor(private val contentRepository: ContentRepository)

fun main(args: Array<String>) {
    runApplication<BanditApplication>(*args)
}

