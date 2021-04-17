package com.funcorp.springbootcontent.content.repository

import com.funcorp.springbootcontent.content.model.Content
import org.springframework.data.mongodb.repository.MongoRepository

interface IContentRepository : MongoRepository<Content, String>