package com.funcorp.bandit.content.model

import com.funcorp.bandit.extensions.toDate
import com.funcorp.bandit.statistics.model.IBanditScorable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "content")
data class Content(
    @Id
    override val id: String = "",

    // TODO: ZonedDateTime ?
    val createdOn: Date = Date(),

    val views: MutableMap<String, ContentEvent> = mutableMapOf<String, ContentEvent>(),
    val likes: MutableMap<String, ContentEvent> = mutableMapOf<String, ContentEvent>()
) : IBanditScorable<String> {
    /**
     * [createdOn] - Unix time (epoch seconds)
     */
    constructor(id: String, createdOn: String) : this(id, createdOn.toDate())

    /**
     * [createdOn] - Unix time (epoch seconds)
     */
    constructor(id: String, createdOn: Long) : this(id, createdOn.toDate())

    override var attempts: Int = 0

    // TODO: calculate in dedicated document / collection ?
    override var statisticalScore: Double = 0.0
}