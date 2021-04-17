package com.funcorp.springbootcontent.integrationtests

import com.funcorp.springbootcontent.content.model.Content
import com.github.javafaker.Faker
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
    fun content_Add() {
        val content = Content(
            id = UUID.randomUUID().toString(),
            createdOn = Instant.now().minus(faker.random().nextLong(20), ChronoUnit.MINUTES).epochSecond
        )

        var result = mockMvc.perform(
            MockMvcRequestBuilders.post("/content/add")
                .queryParam("id", content.id)
                .queryParam(
                    "timestamp", content.createdOn.toInstant().epochSecond.toString()
                )
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)

        result = mockMvc.perform(MockMvcRequestBuilders.get("/content/${content.id}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)

        // TODO: check deserialization of Content
//        log.info(result.andReturn().response.getContentAsString())
    }

    @Test
    fun content_Get() {

    }
}
