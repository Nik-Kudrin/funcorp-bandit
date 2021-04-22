package com.funcorp.bandit.unittests

import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.extensions.toDate
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.time.Instant

class ContentModelTest {
    companion object {
        private val log = LoggerFactory.getLogger(ContentModelTest::class.java)
    }

    // TODO: Just examples of tests. Not complete coverage though.

    @Test
    fun unixTimestamp_ToDate() {
        val expected = Instant.ofEpochSecond(9999999)
        val content = Content(id = "14", createdOn = expected.epochSecond.toDate())

        content.createdOn.toInstant().epochSecond.shouldBe(expected.epochSecond)
    }
}