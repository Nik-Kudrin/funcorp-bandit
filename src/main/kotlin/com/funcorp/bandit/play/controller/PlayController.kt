package com.funcorp.bandit.play.controller

import com.funcorp.bandit.algorithm.Ucb1Algorithm
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/play")
class PlayController {

    @GetMapping(value = ["/{id}"], produces = ["application/json"])
    fun play(@PathVariable("id") id: Long): Any {
        // TODO У сервиса есть HTTP ручка /play/{UserId}, по которой он должен вернуть список из 10
        //ContentId, отсортированный по алгоритму UCB1. Если контента не хватает, то вернуть
        //сколько есть.

        return Ucb1Algorithm(totalItemsNumber = 100).selectOption()
    }
}