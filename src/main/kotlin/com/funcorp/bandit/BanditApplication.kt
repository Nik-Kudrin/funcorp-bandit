package com.funcorp.bandit

import com.funcorp.bandit.cache.CacheNames
import com.funcorp.bandit.content.repository.ContentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling


@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableCaching
class BanditApplication @Autowired constructor(private val contentRepository: ContentRepository) {
    companion object {
        fun main(args: Array<String>) {
            runApplication<BanditApplication>(*args)
        }
    }

    @Bean
    fun cacheManager(): CacheManager {
        return ConcurrentMapCacheManager(CacheNames.CONTENT)
    }
}


