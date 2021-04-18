package com.funcorp.bandit.loadtests

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime
import kotlin.time.milliseconds
import kotlin.time.minutes
import kotlin.time.toDuration

@AutoConfigureMockMvc
@SpringBootTest
class BanditLoadTest {
    // TODO: migrate to TestResBanditLoadTesttTemplate
    @Autowired
    private lateinit var mockMvc: MockMvc

    companion object {
        private val log = LoggerFactory.getLogger(BanditLoadTest::class.java)
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Test
    fun startLoadTest() {
        log.info("Environment preparation is starting ...")
        val duration = measureTime {
            LoadTestEnvironment.prepareUsersPool(mockMvc)
            LoadTestEnvironment.prepareContentData(mockMvc)
        }
        log.info("Environment is prepared. Took $duration")

        val loadRunDuration = 5.minutes
        val startTime = System.currentTimeMillis().toDuration(TimeUnit.MILLISECONDS)

        do {
            scope.launch { LoadTestEnvironment.users.random().act() }
            runBlocking { delay(50.milliseconds) }
        } while (System.currentTimeMillis().toDuration(TimeUnit.MILLISECONDS) < startTime + loadRunDuration)
    }
}