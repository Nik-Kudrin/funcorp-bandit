package com.funcorp.springbootcontent.content.service

import com.funcorp.springbootcontent.content.model.Content
import org.springframework.transaction.annotation.Transactional
import java.util.*

internal interface IContentService {
    @Transactional
    fun insert(content: Content): Boolean

    fun getById(id: String): Optional<Content>
}