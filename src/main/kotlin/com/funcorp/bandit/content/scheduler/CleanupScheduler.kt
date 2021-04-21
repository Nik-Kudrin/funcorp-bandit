package com.funcorp.bandit.content.scheduler

import com.funcorp.bandit.content.service.BanditContentService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.measureTime


@Component
class CleanupScheduler @Autowired constructor(private val contentService: BanditContentService) {
    companion object {
        private val log = LoggerFactory.getLogger(CleanupScheduler::class.java)
    }

    // every 2 minutes
    @Scheduled(fixedRate = 2 * 60 * 1000)
    @Transactional
    fun deleteOutOfDateContent() {
        log.info("Cleanup out of date content is starting ...")

        val duration = measureTime {
            val oldestTimeBoundary = Date.from(Instant.now().minus(30, ChronoUnit.MINUTES))

            // TODO: use MongoTemplate and DynamicQuery to filter unnecessary content on DB side
            val contentToDelete = contentService.getAll()
                .filter { it.createdOn.before(oldestTimeBoundary) }

            contentService.delete(contentToDelete)
        }

        log.info("Cleanup out of date content finished. Took $duration")
    }
}