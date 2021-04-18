package com.funcorp.bandit.content.model

import com.funcorp.bandit.extensions.toDate
import java.util.*

data class ContentEvent(
    val userId: String = "",
    val eventType: EventType = EventType.UNDEFINED,
    val eventTime: Date? = Date()
) {
    constructor(userId: String, eventType: EventType, eventTime: String) : this(userId, eventType, eventTime.toDate())
    constructor(userId: String, eventType: EventType, eventTime: Long) : this(userId, eventType, eventTime.toDate())
}
