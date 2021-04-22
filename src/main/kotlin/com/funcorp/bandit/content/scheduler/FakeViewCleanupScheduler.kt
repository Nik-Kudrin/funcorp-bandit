package com.funcorp.bandit.content.scheduler

import com.funcorp.bandit.content.service.BanditContentService
import com.funcorp.bandit.content.service.FakeViewsContainer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.time.measureTime
import kotlin.time.minutes


@Component
class FakeViewCleanupScheduler @Autowired constructor(private val contentService: BanditContentService) {
    companion object {
        private val log = LoggerFactory.getLogger(FakeViewCleanupScheduler::class.java)
    }

    // every 30 seconds
    @Scheduled(fixedRate = 30 * 1000)
    @Transactional
    fun deleteFakeViewContent() {
        log.info("Removal of fake views is starting ...")

        val duration = measureTime {
            val viewsToRemove = FakeViewsContainer.filterOutOfDateFakeViews(timeLimitDuration = 5.minutes)

            viewsToRemove.onEach {
                FakeViewsContainer.removeFakeView(it.key)
                contentService.deleteFakeView(contentId = it.key.contentId, userId = it.key.userId)
            }
        }

        log.info("Removal of fake views is finished. Took $duration")
    }
}