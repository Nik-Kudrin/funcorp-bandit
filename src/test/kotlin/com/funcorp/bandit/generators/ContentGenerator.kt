package com.funcorp.bandit.generators

import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.extensions.toDate
import com.github.javafaker.Faker
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class ContentGenerator {
    companion object {
        val faker = Faker.instance(Locale("RU"))

        fun generateValidContent(): Content =
            Content(
                id = UUID.randomUUID().toString(),
                createdOn = Instant.now().minus(faker.random().nextLong(5), ChronoUnit.MINUTES).epochSecond.toDate()
            )
    }
}