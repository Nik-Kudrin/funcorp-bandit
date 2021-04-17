package com.funcorp.bandit.content.service

import com.funcorp.bandit.content.model.Content
import org.springframework.transaction.annotation.Transactional
import java.util.*

internal interface IContentService {
    @Transactional
    fun insert(content: Content): Boolean

    fun getById(id: String): Optional<Content>
}