package com.funcorp.bandit.content.service

import com.funcorp.bandit.content.model.Content
import org.springframework.transaction.annotation.Transactional
import java.util.*

internal interface ContentService {
    @Transactional
    fun insert(content: Content): Boolean

    @Transactional
    fun save(content: Content): Optional<Content>

    fun getById(id: String): Optional<Content>

    fun getAll(): List<Content>
}