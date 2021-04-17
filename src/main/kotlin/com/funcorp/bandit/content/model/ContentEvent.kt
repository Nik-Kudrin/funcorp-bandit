package com.funcorp.bandit.content.model

import java.util.*

enum class EventType {
    View,
    Like
}

data class ContentEvent(
    val userId: String,
    val eventType: EventType,
    val eventTime: Date = Date()
)
