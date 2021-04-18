package com.funcorp.bandit.statistics.controller

import com.funcorp.bandit.algorithm.Ucb1Algorithm
import com.funcorp.bandit.content.service.ContentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/play")
class StatisticsController {
    @Autowired
    private lateinit var contentService: ContentService

    companion object {
        private val ucb1Algorithm = Ucb1Algorithm()
    }

    @Transactional // TODO: potential bottleneck ?
    @GetMapping(value = ["/{userId}"], produces = ["application/json"])
    fun play(@PathVariable("userId") id: String): List<String> {
        // TODO У сервиса есть HTTP ручка /play/{UserId}, по которой он должен вернуть список из 10
        //ContentId, отсортированный по алгоритму UCB1. Если контента не хватает, то вернуть
        //сколько есть.


        val contentItems = contentService.getAll()

        val promisingItems = ucb1Algorithm.selectMostPromisingItems(contentItems, itemsCount = 10)
        // TODO: В течении 5 минут после отдачи списка контента, если событий просмотра не было,
        // считается, что этот пользователь этот контент просмотрел
        // TODO: create db scheduler ?
        return promisingItems
    }
}