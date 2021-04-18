package com.funcorp.bandit.loadtests

import com.funcorp.bandit.integrationtests.addLikeToContentViaHttp
import com.funcorp.bandit.integrationtests.addViewToContentViaHttp
import com.funcorp.bandit.integrationtests.deserializeResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import java.util.*
import kotlin.random.Random
import kotlin.time.seconds

class VirtualUser(private val mockMvc: MockMvc) {
    val userId: String = UUID.randomUUID().toString()

    companion object {
        private val log = LoggerFactory.getLogger(VirtualUser::class.java)
    }

    fun getSuggestedContent(): List<String> {
        val resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/play/${UUID.randomUUID()}"))
        log.debug("Request executed on /play endpoint")

        return resultActions.deserializeResponse<List<String>>()
    }

    fun act() {
        val contentList = getSuggestedContent()

        runBlocking { delay(15.seconds) }

        val contentToWatch = contentList.take(Random.nextInt(until = contentList.size))
        contentToWatch.forEach {
            mockMvc.addViewToContentViaHttp(it, userId)
            runBlocking { delay(5.seconds) }
        }

        val contentToLike = contentToWatch
            .filter { Random.nextDouble(0.05) > LoadTestEnvironment.getContentDistributionPercentage(it) }
        contentToLike.forEach {
            mockMvc.addLikeToContentViaHttp(it, userId)
            runBlocking { delay(5.seconds) }
        }
    }
}