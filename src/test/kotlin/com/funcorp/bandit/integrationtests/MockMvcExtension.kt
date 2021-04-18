package com.funcorp.bandit.integrationtests

import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.generators.ContentGenerator
import com.funcorp.bandit.loadtests.BanditLoadTest
import org.slf4j.LoggerFactory
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.util.*

private val log = LoggerFactory.getLogger(BanditLoadTest::class.java)

internal fun MockMvc.addContentViaHttp(): Content {
    val expectedContent = ContentGenerator.generateValidContent()

    this.perform(
        MockMvcRequestBuilders.post("$CONTENT_ROUTE/add")
            .queryParam("id", expectedContent.id)
            .queryParam("timestamp", expectedContent.createdOn.toInstant().epochSecond.toString())
    ).andExpect(MockMvcResultMatchers.status().isCreated)

    log.info("Request executed for adding content ${expectedContent.id}")

    return expectedContent
}

internal fun MockMvc.addLikeToContentViaHttp(
    contentId: String,
    userId: String = UUID.randomUUID().toString()
): Pair<String, ContentEvent> {
    val like = ContentEvent(userId, EventType.Like, Instant.now().epochSecond.toString())

    this.perform(
        MockMvcRequestBuilders.post("$CONTENT_ROUTE/$contentId/likes/add")
            .queryParam("userId", userId)
            .queryParam("likedOn", like.eventTime.toInstant().epochSecond.toString())
    ).andExpect(MockMvcResultMatchers.status().isOk)

    log.info("Request executed to add like to content $contentId")

    return Pair(userId, like)
}

internal fun MockMvc.addViewToContentViaHttp(
    contentId: String,
    userId: String = UUID.randomUUID().toString()
): Pair<String, ContentEvent> {
    val view = ContentEvent(userId, EventType.View, Instant.now().epochSecond.toString())

    this.perform(
        MockMvcRequestBuilders.post("$CONTENT_ROUTE/$contentId/views/add")
            .queryParam("userId", userId)
            .queryParam("watchedOn", view.eventTime.toInstant().epochSecond.toString())
    ).andExpect(MockMvcResultMatchers.status().isOk)

    log.info("Request executed to add view to content $contentId")

    return Pair(userId, view)
}

internal fun MockMvc.getContentViaHttp(contentId: String): Content {
    val response = this.perform(MockMvcRequestBuilders.get("$CONTENT_ROUTE/$contentId"))
        .andExpect(MockMvcResultMatchers.status().isOk)

    log.info("Request executed to get content $contentId")

    return response.deserializeResponse<Content>()
}