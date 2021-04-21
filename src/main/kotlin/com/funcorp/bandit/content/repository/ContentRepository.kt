package com.funcorp.bandit.content.repository

import com.funcorp.bandit.content.model.Content
import org.springframework.data.mongodb.repository.MongoRepository

interface ContentRepository : MongoRepository<Content, String>