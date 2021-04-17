package com.funcorp.springbootcontent.unittests

import com.funcorp.springbootcontent.content.model.Content
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.Instant

class ContentModelTest {
    companion object {
        private val log = LoggerFactory.getLogger(ContentModelTest::class.java)
    }

    @Test
    fun unixTimestamp_ToDate() {
        val expected = Instant.ofEpochSecond(9999999)
        val content = Content(id = "14", createdOn = expected.epochSecond)

        content.createdOn.toInstant().epochSecond.shouldBe(expected.epochSecond)
    }
}