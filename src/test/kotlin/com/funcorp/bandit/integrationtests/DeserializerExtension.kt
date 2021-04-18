package com.funcorp.bandit.integrationtests

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.test.web.servlet.ResultActions

private val mapper = ObjectMapper()

internal inline fun <reified T> ResultActions.deserializeResponse(): T {
    return mapper.readValue(this.andReturn().response.contentAsString, T::class.java)
}