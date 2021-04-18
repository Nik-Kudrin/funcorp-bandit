package com.funcorp.bandit.unittests

import com.funcorp.bandit.algorithm.Ucb1Algorithm
import com.funcorp.bandit.generators.ContentGenerator
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class UcbAlgorithmTest {
    companion object {
        private val ucb1Algorithm = Ucb1Algorithm()
    }

    @Test
    fun takeFirstNonExploredItems() {
        val contentItems = (0..20).map { ContentGenerator.generateValidContent() }

        val promisingItems = ucb1Algorithm.selectMostPromisingItems(contentItems, itemsCount = 10)
        promisingItems.shouldBe(contentItems.take(n = 10))
    }
}