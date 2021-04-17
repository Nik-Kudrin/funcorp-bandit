package com.funcorp.springbootcontent.content.service

import com.funcorp.springbootcontent.content.model.Content
import com.funcorp.springbootcontent.content.repository.ContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ContentService : IContentService {
    @Autowired
    private lateinit var contentRepository: ContentRepository

    override fun insert(content: Content): Boolean {
        return if (contentRepository.findById(content.id).isPresent) false
        else {
            contentRepository.saveAndFlush(content)
            true
        }
    }

    override fun getById(id: String) = contentRepository.findById(id)
}