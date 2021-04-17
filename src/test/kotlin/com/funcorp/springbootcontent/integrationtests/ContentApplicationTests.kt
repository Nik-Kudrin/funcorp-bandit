package com.funcorp.springbootcontent.integrationtests

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

@SpringBootTest
@AutoConfigureMockMvc
class ContentApplicationTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

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
        val result = mockMvc.perform(
            MockMvcRequestBuilders.post("/content/add")
                .queryParam("id", "13")
                .queryParam("timestamp", Instant.now().epochSecond.toString())
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
        log.info(result.toString())
    }

    @Test
    fun content_Get() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/content/13"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
        log.info(result.toString())
    }
}
