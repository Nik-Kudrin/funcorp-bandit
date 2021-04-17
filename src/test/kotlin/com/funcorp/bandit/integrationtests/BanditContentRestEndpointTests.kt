package com.funcorp.bandit.integrationtests

import com.fasterxml.jackson.databind.ObjectMapper
import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.github.javafaker.Faker
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class BanditContentRestEndpointTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val faker = Faker.instance(Locale("RU"))
    private val mapper = ObjectMapper()
    private final val CONTENT_ROUTE = "/content"

    companion object {
        private val log = LoggerFactory.getLogger(BanditContentRestEndpointTests::class.java)
    }

    private fun addContentViaHttp(): Content {
        val expectedContent = Content(
            id = UUID.randomUUID().toString(),
            createdOn = Instant.now().minus(faker.random().nextLong(20), ChronoUnit.MINUTES).epochSecond
        )

        mockMvc.perform(
            MockMvcRequestBuilders.post("$CONTENT_ROUTE/add")
                .queryParam("id", expectedContent.id)
                .queryParam("timestamp", expectedContent.createdOn.toInstant().epochSecond.toString())
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        return expectedContent
    }

    private inline fun <reified T> ResultActions.deserializeResponse(): T {
        return mapper.readValue(this.andReturn().response.contentAsString, T::class.java)
    }

    private fun getContentViaHttp(contentId: String): Content {
        val response = mockMvc.perform(MockMvcRequestBuilders.get("$CONTENT_ROUTE/$contentId"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        return response.deserializeResponse<Content>()
    }

    @Test
    fun play_Get() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/play/13"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)

        // TODO: validate
        log.info(result.toString())
    }

    @Test
    fun contentStoredInDbSuccessfully() {
        val expectedContent = addContentViaHttp()
        val storedContent = getContentViaHttp(expectedContent.id)
        storedContent.shouldBe(expectedContent)
    }

    @Test
    fun likeShouldBeStoredInDb() {
        val expectedContent = addContentViaHttp()
        val userId = UUID.randomUUID().toString()
        val like = ContentEvent(userId, EventType.Like, Instant.now().epochSecond.toString())

        mockMvc.perform(
            MockMvcRequestBuilders.post("$CONTENT_ROUTE/${expectedContent.id}/likes/add")
                .queryParam("userId", userId)
                .queryParam("likedOn", like.eventTime.toInstant().epochSecond.toString())
        ).andExpect(MockMvcResultMatchers.status().isOk)

        val storedContent = getContentViaHttp(expectedContent.id)

        expectedContent.likes.putIfAbsent(userId, like)

        storedContent.shouldBe(expectedContent)
    }
}
