package com.funcorp.bandit.content.service

import com.funcorp.bandit.algorithm.AverageUpdateStrategy
import com.funcorp.bandit.algorithm.ICalculateScoreStrategy
import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.content.model.ContentEvent
import com.funcorp.bandit.content.model.EventType
import com.funcorp.bandit.content.repository.IContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ContentService : IContentService {
    @Autowired
    private lateinit var contentRepository: IContentRepository

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
    fun addView(contentId: String, userId: String, watchedOn: String): Optional<Content> {
        val optional = contentRepository.findById(contentId)

        if (optional.isEmpty) return Optional.empty()

        val content = optional.get()
        content.attempts++
        content.views.putIfAbsent(userId, ContentEvent(userId, EventType.View, watchedOn))

        return Optional.of(contentRepository.save(content))
    }

    override fun getById(id: String) = contentRepository.findById(id)

    override fun getAll(): List<Content> = contentRepository.findAll()

    fun delete(content: Iterable<Content>) = contentRepository.deleteAll(content)
}