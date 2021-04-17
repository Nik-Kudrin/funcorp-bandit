package com.funcorp.bandit.integrationtests

import com.fasterxml.jackson.databind.ObjectMapper
import com.funcorp.bandit.content.model.Content
import com.github.javafaker.Faker
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class ContentApplicationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val faker = Faker.instance(Locale("RU"))
    private val mapper = ObjectMapper()

    companion object {
        private val log = LoggerFactory.getLogger(ContentApplicationTests::class.java)
    }

    @Test
    fun play_Get() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/play/13"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
        log.info(result.toString())
    }

    @Test
    fun contentStoredInDbSuccessfully() {
        val expectedContent = Content(
            id = UUID.randomUUID().toString(),
            createdOn = Instant.now().minus(faker.random().nextLong(20), ChronoUnit.MINUTES).epochSecond
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("/content/add")
                .queryParam("id", expectedContent.id)
                .queryParam("timestamp", expectedContent.createdOn.toInstant().epochSecond.toString())
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        val response = mockMvc.perform(MockMvcRequestBuilders.get("/content/${expectedContent.id}"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val storedContent = mapper.readValue(
            response.andReturn().response.contentAsString,
            Content::class.java
        )

        storedContent.shouldBe(expectedContent)
    }
}
