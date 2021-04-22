package com.funcorp.bandit.content.model

import com.funcorp.bandit.statistics.model.BanditScorable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "content")
data class Content(
    @Id
    override val id: String = "",

    val createdOn: Date = Date(),

    /**
     * Views: <UserId / ContentEvent>
     */
    val views: MutableMap<String, ContentEvent> = mutableMapOf<String, ContentEvent>(),

    /**
     * Likes: <UserId / ContentEvent>
     */
    val likes: MutableMap<String, ContentEvent> = mutableMapOf<String, ContentEvent>(),

    override var attempts: Long = 0L,

    override var statisticalScore: Double = 0.0
) : BanditScorable<String>