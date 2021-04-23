package com.funcorp.bandit.content.service

import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.minutes

data class FakeViewKey(val contentId: String, val userId: String)

object FakeViewsContainer {
    /**
     * Contains content with "fake" views <ContentId:UserId, InsertionTime>>
     * "Fake" views will be outdated after some time
     */
    private val fakeViews = ConcurrentHashMap<FakeViewKey, Date>()

    fun addFakeView(contentId: String, userId: String, insertionTime: Date) {
        fakeViews[com.funcorp.bandit.content.service.FakeViewKey(contentId, userId)] = insertionTime
    }

    fun removeFakeView(key: FakeViewKey) = fakeViews.remove(key)

    fun filterOutOfDateFakeViews(timeLimitDuration: Duration = 5.minutes): Map<FakeViewKey, Date> {
        val timeLimitBoundary = Date.from(Instant.now().minusMillis(timeLimitDuration.toLongMilliseconds()))
        return fakeViews.filter { it.value.before(timeLimitBoundary) }
    }
}