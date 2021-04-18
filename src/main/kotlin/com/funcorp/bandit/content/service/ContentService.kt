package com.funcorp.bandit.content.service

import com.funcorp.bandit.algorithm.AverageUpdateStrategy
import com.funcorp.bandit.algorithm.ICalculateScoreStrategy
import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.content.model.FAKE_VIEW_DATE
import com.funcorp.bandit.content.repository.IContentRepository
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.time.minutes

@Service
class ContentService : IContentService {
    @Autowired
    private lateinit var contentRepository: IContentRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private val updateStrategy: ICalculateScoreStrategy = AverageUpdateStrategy()
    }

    @Transactional
    override fun insert(content: Content): Boolean {
        return if (contentRepository.findById(content.id).isPresent) false
        else {
            contentRepository.insert(content)
            true
        }
    }

    @Transactional
    override fun save(content: Content): Optional<Content> {
        return if (contentRepository.findById(content.id).isPresent)
            Optional.of(contentRepository.save(content))
        else Optional.empty()
    }

    @Transactional
    fun addLike(contentId: String, userId: String, likedOn: String): Optional<Content> {
        val optional = contentRepository.findById(contentId)

        if (optional.isEmpty) return Optional.empty()

        val content = optional.get()
        // Recalculate score
        content.statisticalScore = updateStrategy.calculateScore(content.attempts, content.statisticalScore, 1.0)
        content.likes.putIfAbsent(userId, ContentEvent(userId, EventType.Like, likedOn))

        return Optional.of(contentRepository.save(content))
    }

    @Transactional
    fun addView(contentId: String, userId: String, watchedOn: String = ""): Optional<Content> {
        val optional = contentRepository.findById(contentId)

        if (optional.isEmpty) return Optional.empty()

        val content = optional.get()

        // "fake" view for user, when it just got content (this view will be outdated after some time)
        val view: ContentEvent = if (watchedOn.isBlank())
            ContentEvent(userId, EventType.View, FAKE_VIEW_DATE)
        else
            ContentEvent(userId, EventType.View, watchedOn)

        // increase counter only in case, if there was no view before
        if (!content.views.containsKey(userId))
            content.attempts++

        content.views[userId] = view

        return Optional.of(contentRepository.save(content))
    }

    @Transactional
    fun deleteFakeView(contentId: String, userId: String) {
        val optional = contentRepository.findById(contentId)

        if (optional.isEmpty) return

        val content = optional.get()
        content.attempts--
        content.views.remove(userId)
        contentRepository.save(content)
    }

    /**
     * After 5 minutes "fake" views will be deleted
     */
    fun fakeViewActualizator(contentId: String, userId: String) {
        scope.launch {
            delay(5.minutes)
            val optional = getById(contentId)

            if (optional.isPresent) {
                val view = optional.get().views[userId]
                if (view != null && view.eventTime == FAKE_VIEW_DATE)
                    deleteFakeView(contentId, userId)
            }
        }
    }

    override fun getById(id: String) = contentRepository.findById(id)

    override fun getAll(): List<Content> = contentRepository.findAll()

    fun delete(content: Iterable<Content>) = contentRepository.deleteAll(content)
}