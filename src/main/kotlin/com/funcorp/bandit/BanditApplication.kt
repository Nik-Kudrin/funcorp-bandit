package com.funcorp.bandit

import com.funcorp.bandit.content.repository.IContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

@EnableAutoConfiguration
@SpringBootApplication
class BanditApplication {
    @Autowired
    private lateinit var contentRepository: IContentRepository

    @EventListener(ApplicationReadyEvent::class)
    fun runAfterStartup() {
        // TODO: reload latest stored date (in case of shutdown)
    }

    fun runBeforeShutdown() {
        // TODO: store date in DB ? (redis ?)
    }
}

fun main(args: Array<String>) {
    runApplication<BanditApplication>(*args)
}

