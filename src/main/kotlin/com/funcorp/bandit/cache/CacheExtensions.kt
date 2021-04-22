package com.funcorp.bandit.cache

import com.funcorp.bandit.content.model.Content
import org.slf4j.LoggerFactory
import org.springframework.cache.CacheManager
import java.util.*

class CacheExtensions {
    companion object {
        private val log = LoggerFactory.getLogger(CacheExtensions::class.java)

        val CacheManager.contentCache
            get() = requireNotNull(this.getCache(CacheNames.CONTENT)) {
                "Cache '${CacheNames.CONTENT}' is null"
            }

        fun CacheManager.getCachedContent(contentId: String): Optional<Content> =
            Optional.ofNullable(contentCache.get(contentId, Content::class.java))

        fun CacheManager.putContentInCache(content: Content): Unit {
            contentCache.put(content.id, content)
        }
    }
}