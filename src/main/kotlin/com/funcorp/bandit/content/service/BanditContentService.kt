package com.funcorp.bandit.content.service

import com.funcorp.bandit.algorithm.AverageScoreStrategy
import com.funcorp.bandit.algorithm.ScoreStrategy
import com.funcorp.bandit.cache.CacheExtensions.Companion.contentCache
import com.funcorp.bandit.cache.CacheExtensions.Companion.getCachedContent
import com.funcorp.bandit.cache.CacheExtensions.Companion.putContentInCache
import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.content.model.FAKE_VIEW_DATE
import com.funcorp.bandit.content.repository.ContentRepository
import com.funcorp.bandit.extensions.toDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class BanditContentService @Autowired constructor(
    private val contentRepository: ContentRepository,
    private val cacheManager: CacheManager
) : ContentService {
    companion object {
        private val log = LoggerFactory.getLogger(BanditContentService::class.java)
        private val scoreStrategy: ScoreStrategy = AverageScoreStrategy()
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)


    /**
     * Return true - successful insert
     * False - otherwise
     */
    fun insert(content: Content): Boolean {
        if (cacheManager.getCachedContent(content.id).isPresent) return true
        else cacheManager.putContentInCache(content)

        return if (contentRepository.findById(content.id).isPresent) true
        else {
            contentRepository.insert(content)
            true
        }
    }

    fun save(content: Content): Unit {
        cacheManager.putContentInCache(content)

        scope.launch {
            try {
                contentRepository.save(content)
            } catch (e: Exception) {
                log.error("Error saving content '${content.id}' ${e.stackTraceToString()}")
            }
        }
    }

    fun addLike(contentId: String, userId: String, likedOn: String): Optional<Content> {
        val optional = getById(contentId)
        if (optional.isEmpty) return optional

        val content = optional.get()
        // Recalculate score
        content.statisticalScore = scoreStrategy.calculateScore(content.attempts, content.statisticalScore, 1.0)
        content.likes.putIfAbsent(userId, ContentEvent(userId, EventType.LIKE, likedOn.toDate()))

        save(content)
        return Optional.of(content)
    }

    fun addView(contentId: String, userId: String, watchedOn: String = ""): Optional<Content> {
        val optional = getById(contentId)
        if (optional.isEmpty) return optional

        val content = optional.get()

        // "fake" view for user, when it just got content (this view will be outdated after some time)
        val view: ContentEvent = if (watchedOn.isBlank()) {
            FakeViewsContainer.addFakeView(contentId, userId, Instant.now().epochSecond.toDate())
            ContentEvent(userId, EventType.VIEW, FAKE_VIEW_DATE)
        } else
            ContentEvent(userId, EventType.VIEW, watchedOn.toDate())

        // increase counter only in case, if there was no view before
        if (!content.views.containsKey(userId))
            content.attempts++

        content.views[userId] = view

        save(content)
        return Optional.of(content)
    }

    fun deleteFakeView(contentId: String, userId: String) {
        val optional = getById(contentId)
        if (optional.isEmpty) return

        val content = optional.get()
        content.attempts--
        content.views.remove(userId)
        save(content)
    }

    fun getById(id: String): Optional<Content> {
        var optional = cacheManager.getCachedContent(id)

        return if (optional.isPresent) optional
        else {
            optional = contentRepository.findById(id)
            if (optional.isPresent) cacheManager.putContentInCache(optional.get())
            optional
        }
    }

    fun getAll(): List<Content> = contentRepository.findAll()

    fun delete(content: Iterable<Content>) {
        scope.launch { content.onEach { cacheManager.contentCache.evict(it.id) } }
        scope.launch { contentRepository.deleteAll(content) }
    }
}