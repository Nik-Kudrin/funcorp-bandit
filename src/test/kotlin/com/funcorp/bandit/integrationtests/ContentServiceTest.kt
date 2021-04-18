package com.funcorp.bandit.integrationtests

import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.content.service.ContentService
import com.funcorp.bandit.generators.ContentGenerator
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.Instant
import java.util.*

@SpringBootTest
class ContentServiceTest {
    @Autowired
    private lateinit var contentService: ContentService

    @Test
    fun insertContentTest() {
        val expectedContent = ContentGenerator.generateValidContent()
        contentService.insert(expectedContent).shouldBeTrue()
    }

    @Test
    fun retrieveContentTest() {
        val expectedContent = ContentGenerator.generateValidContent()
        contentService.insert(expectedContent)
        val storedContent = contentService.getById(expectedContent.id)
        storedContent.get().shouldBe(expectedContent)
    }

    @Test
    fun addViewToContentTest() {
        val expectedContent = ContentGenerator.generateValidContent()
        val userId = UUID.randomUUID().toString()
        val view = ContentEvent(userId, EventType.View, Instant.now().epochSecond.toString())

        contentService.let {
            it.insert(expectedContent)
            it.addView(expectedContent.id, userId, watchedOn = view.eventTime.toInstant().epochSecond.toString())
        }
            .get()
            .shouldBe(expectedContent.apply { views.putIfAbsent(userId, view) })
    }

    @Test
    fun addLikeToContentTest() {
        val expectedContent = ContentGenerator.generateValidContent()
        val userId = UUID.randomUUID().toString()
        val like = ContentEvent(userId, EventType.Like, Instant.now().epochSecond.toString())

        contentService.let {
            it.insert(expectedContent)
            it.addLike(expectedContent.id, userId, likedOn = like.eventTime.toInstant().epochSecond.toString())
        }
            .get()
            .shouldBe(expectedContent.apply { likes.putIfAbsent(userId, like) })
    }

    @Test
    fun addLikeScoreCalculationTest() {
        TODO("Not implemented yet")
    }
}