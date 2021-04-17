package com.funcorp.springbootcontent.content.repository

import com.funcorp.springbootcontent.content.model.Content
import org.springframework.data.jpa.repository.JpaRepository

interface ContentRepository : JpaRepository<Content, String>