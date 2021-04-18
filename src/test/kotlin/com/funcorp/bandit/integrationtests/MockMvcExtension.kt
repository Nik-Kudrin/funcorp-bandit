package com.funcorp.bandit.integrationtests

import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.generators.ContentGenerator
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.Instant
import java.util.*

internal fun MockMvc.addContentViaHttp(): Content {
    val expectedContent = ContentGenerator.generateValidContent()

    this.perform(
        MockMvcRequestBuilders.post("$CONTENT_ROUTE/add")
            .queryParam("id", expectedContent.id)
            .queryParam("timestamp", expectedContent.createdOn.toInstant().epochSecond.toString())
    ).andExpect(MockMvcResultMatchers.status().isCreated)

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

    return Pair(userId, view)
}

internal fun MockMvc.getContentViaHttp(contentId: String): Content {
    val response = this.perform(MockMvcRequestBuilders.get("$CONTENT_ROUTE/$contentId"))
        .andExpect(MockMvcResultMatchers.status().isOk)

    return response.deserializeResponse<Content>()
}