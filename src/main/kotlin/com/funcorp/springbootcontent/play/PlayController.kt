package com.funcorp.springbootcontent.play

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/play")
class PlayController {

    @GetMapping(value = ["/{id}"], produces = ["application/json"])
    fun play(@PathVariable("id") id: Long): Any {
        return 1
    }
}