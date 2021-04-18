package com.funcorp.bandit.integrationtests

import com.fasterxml.jackson.databind.ObjectMapper
import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.generators.ContentGenerator
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.util.*


@SpringBootTest
@AutoConfigureMockMvc
class BanditContentRestEndpointTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = ObjectMapper()
    private final val CONTENT_ROUTE = "/content"

    companion object {
        private val log = LoggerFactory.getLogger(BanditContentRestEndpointTests::class.java)
    }

    private fun addContentViaHttp(): Content {
        val expectedContent = ContentGenerator.generateValidContent()

        mockMvc.perform(
            MockMvcRequestBuilders.post("$CONTENT_ROUTE/add")
                .queryParam("id", expectedContent.id)
                .queryParam("timestamp", expectedContent.createdOn.toInstant().epochSecond.toString())
        ).andExpect(MockMvcResultMatchers.status().isCreated)

        return expectedContent
    }

    private fun addLikeToContentViaHttp(contentId: String): Pair<String, ContentEvent> {
        val userId = UUID.randomUUID().toString()
        val like = ContentEvent(userId, EventType.Like, Instant.now().epochSecond.toString())

        mockMvc.perform(
            MockMvcRequestBuilders.post("$CONTENT_ROUTE/$contentId/likes/add")
                .queryParam("userId", userId)
                .queryParam("likedOn", like.eventTime!!.toInstant().epochSecond.toString())
        ).andExpect(MockMvcResultMatchers.status().isOk)

        return Pair(userId, like)
    }

    private fun addViewToContentViaHttp(contentId: String): Pair<String, ContentEvent> {
        val userId = UUID.randomUUID().toString()
        val view = ContentEvent(userId, EventType.View, Instant.now().epochSecond.toString())

        mockMvc.perform(
            MockMvcRequestBuilders.post("$CONTENT_ROUTE/$contentId/views/add")
                .queryParam("userId", userId)
                .queryParam("watchedOn", view.eventTime!!.toInstant().epochSecond.toString())
        ).andExpect(MockMvcResultMatchers.status().isOk)

        return Pair(userId, view)
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
    fun play_GetContentShouldReturnMostPromisingItems() {
        val content = (0 until 30).map { addContentViaHttp() }

        // view all content
        content.forEach { addViewToContentViaHttp(it.id) }

        val likedContent = content.shuffled().take(7).apply { forEach { addLikeToContentViaHttp(it.id) } }

        val resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/play/${UUID.randomUUID()}"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val prioritizedItems = resultActions.deserializeResponse<List<String>>()

        prioritizedItems.shouldContainAll(likedContent.map { it.id })
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

        val (userId, like) = addLikeToContentViaHttp(expectedContent.id)
        val storedContent = getContentViaHttp(expectedContent.id)

        expectedContent.likes.putIfAbsent(userId, like)
        storedContent.shouldBe(expectedContent)
    }

    @Test
    fun viewShouldBeStoredInDb() {
        val expectedContent = addContentViaHttp()

        val (userId, view) = addViewToContentViaHttp(expectedContent.id)
        val storedContent = getContentViaHttp(expectedContent.id)

        expectedContent.apply {
            views.putIfAbsent(userId, view)
            attempts++
        }
        storedContent.shouldBe(expectedContent)
    }
}
