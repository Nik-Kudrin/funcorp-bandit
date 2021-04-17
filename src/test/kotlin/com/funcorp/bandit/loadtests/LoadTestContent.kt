package com.funcorp.bandit.loadtests

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoadTestContent {
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    companion object {
        private val log = LoggerFactory.getLogger(LoadTestContent::class.java)
    }

    @Test
    fun play_Get() {
        val entity = restTemplate.getForEntity<String>("/play/13")

        entity.statusCode.shouldBe(HttpStatus.OK)
        entity.body!!.toInt().shouldBe(1)
    }
}