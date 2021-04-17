package com.funcorp.bandit.content.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "content")
data class Content(
    @Id
    val id: String = "",

    // TODO: Zoned Date Time ?
    val createdOn: Date = Date(),

    val events: MutableList<ContentEvent> = mutableListOf()
) {
    /**
     * [createdOn] - unix time (epoch seconds)
     */
    constructor(id: String, createdOn: String) : this(
        id,
        createdOn.toLong()
    )

    /**
     * [createdOn] - unix time (epoch seconds)
     */
    constructor(id: String, createdOn: Long) : this(
        id,
        // to milliseconds
        Date(createdOn * 1000)
    )
}