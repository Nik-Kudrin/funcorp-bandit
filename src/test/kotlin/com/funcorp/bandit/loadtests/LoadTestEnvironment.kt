package com.funcorp.bandit.loadtests

import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.integrationtests.addContentViaHttp
import org.slf4j.LoggerFactory
import org.springframework.test.web.servlet.MockMvc
import kotlin.random.Random

class LoadTestEnvironment {
    companion object {
        private val log = LoggerFactory.getLogger(BanditLoadTest::class.java)

        lateinit var users: List<VirtualUser>
        lateinit var content: Map<String, Pair<Content, Double>>

        // TODO: maintain 3_000 content items all time during test ... (it will clean up after 30 min by scheduler)

        fun prepareContentData(mockMvc: MockMvc, contentNumber: Int = 3_000) {
            log.info("Preparing $contentNumber content items ...")

            content = (0 until contentNumber).map {
                val item = mockMvc.addContentViaHttp()
                item.id to Pair(item, Random.nextDouble(0.0, 0.05))
            }.toMap()

            log.info("Content prepared")
        }

        fun prepareUsersPool(mockMvc: MockMvc, usersNumber: Int = 10_000) {
            log.info("Preparing $usersNumber users ...")
            users = (0 until usersNumber).map { VirtualUser(mockMvc) }
        }

        fun getContentDistributionPercentage(contentId: String): Double {
            val value = content[contentId]
            return value?.second ?: 0.0
        }
    }
}