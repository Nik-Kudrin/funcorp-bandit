package com.funcorp.bandit.content.model

import com.funcorp.bandit.extensions.toDate
import java.time.Instant
import java.util.*

val FAKE_VIEW_DATE = Instant.MIN.epochSecond.toDate()

data class ContentEvent(
    val userId: String = "",
    val eventType: EventType = EventType.UNDEFINED,
    val eventTime: Date = FAKE_VIEW_DATE
)
