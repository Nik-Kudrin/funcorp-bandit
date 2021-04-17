package com.funcorp.bandit.content.model

import com.funcorp.bandit.extensions.toDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "content")
data class Content(
    @Id
    val id: String = "",

    // TODO: Zoned Date Time ?
    val createdOn: Date = Date(),

    val views: MutableMap<String, ContentEvent> = mutableMapOf<String, ContentEvent>(),
    val likes: MutableMap<String, ContentEvent> = mutableMapOf<String, ContentEvent>()
) {
    /**
     * [createdOn] - Unix time (epoch seconds)
     */
    constructor(id: String, createdOn: String) : this(id, createdOn.toDate())

    /**
     * [createdOn] - Unix time (epoch seconds)
     */
    constructor(id: String, createdOn: Long) : this(id, createdOn.toDate())
}