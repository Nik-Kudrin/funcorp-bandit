package com.funcorp.bandit.integrationtests

import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

internal const val CONTENT_ROUTE = "/content"

@SpringBootTest
@AutoConfigureMockMvc
class BanditContentRestEndpointTests @Autowired constructor(private val mockMvc: MockMvc) {
    companion object {
        private val log = LoggerFactory.getLogger(BanditContentRestEndpointTests::class.java)
    }

    // TODO: Just examples of tests. Not complete coverage though.

    @Test
    fun play_GetContentShouldReturnMostPromisingItems() {
        val content = (0 until 30).map { mockMvc.addContentViaHttp() }

        // view all content
        content.forEach { mockMvc.addViewToContentViaHttp(it.id) }

        val likedContent = content.shuffled().take(7).onEach { mockMvc.addLikeToContentViaHttp(it.id) }

        val resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/play/${UUID.randomUUID()}"))
            .andExpect(MockMvcResultMatchers.status().isOk)

        val prioritizedItems = resultActions.deserializeResponse<List<String>>()

        prioritizedItems.shouldContainAll(likedContent.map { it.id })
    }

    @Test
    fun contentStoredInDbSuccessfully() {
        val expectedContent = mockMvc.addContentViaHttp()
        val storedContent = mockMvc.getContentViaHttp(expectedContent.id)
        storedContent.shouldBe(expectedContent)
    }

    @Test
    fun likeShouldBeStoredInDb() {
        val expectedContent = mockMvc.addContentViaHttp()

        val (userId, like) = mockMvc.addLikeToContentViaHttp(expectedContent.id)
        val storedContent = mockMvc.getContentViaHttp(expectedContent.id)

        expectedContent.likes.putIfAbsent(userId, like)
        storedContent.shouldBe(expectedContent)
    }

    @Test
    fun viewShouldBeStoredInDb() {
        val expectedContent = mockMvc.addContentViaHttp()

        val (userId, view) = mockMvc.addViewToContentViaHttp(expectedContent.id)
        val storedContent = mockMvc.getContentViaHttp(expectedContent.id)

        expectedContent.apply {
            views[userId] = view
            attempts++
        }
        storedContent.shouldBe(expectedContent)
    }
}
