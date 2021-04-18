package com.funcorp.bandit.unittests

import com.funcorp.bandit.algorithm.AverageUpdateStrategy
import com.funcorp.bandit.algorithm.ICalculateScoreStrategy
import com.funcorp.bandit.algorithm.Ucb1Algorithm
import com.funcorp.bandit.content.model.Content
import com.funcorp.bandit.generators.ContentGenerator
import com.funcorp.bandit.integrationtests.BanditContentRestEndpointTests
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.doubles.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.slf4j.LoggerFactory
import java.util.stream.Stream

class UcbAlgorithmTest {
    companion object {
        private val ucb1Algorithm = Ucb1Algorithm()
        private val updateStrategy: ICalculateScoreStrategy = AverageUpdateStrategy()
        private val log = LoggerFactory.getLogger(BanditContentRestEndpointTests::class.java)

        @JvmStatic
        private fun takeFirstNonExploredItemsDataProvider(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(20, 10, 10),
                Arguments.of(20, 20, 20),
                Arguments.of(20, 21, 20),
                Arguments.of(0, 4, 0),
                Arguments.of(20, 0, 0),
                Arguments.of(10_000, 10_000, 10_000),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("takeFirstNonExploredItemsDataProvider")
    fun takeFirstNonExploredItems(
        contentItemsCount: Int,
        requestedItemsNumberToReturn: Int,
        expectedItemsNumberReturned: Int
    ) {
        val contentItems = if (contentItemsCount == 0) mutableListOf<Content>()
        else (0 until contentItemsCount).map { ContentGenerator.generateValidContent() }

        val promisingItems = ucb1Algorithm
            .selectMostPromisingItems(contentItems, itemsCount = requestedItemsNumberToReturn)

        promisingItems.size.shouldBe(expectedItemsNumberReturned)
        promisingItems.shouldBe(contentItems.take(n = expectedItemsNumberReturned).map { it.id })
    }

    @Test
    fun scoreCalculationTest() {
        val contentItemsCount: Int = 10
        val requestedItemsNumberToReturn: Int = 5

        val contentItems = (0 until contentItemsCount).map { ContentGenerator.generateValidContent() }

        for (index in 0 until ContentGenerator.faker.random()
            .nextInt(requestedItemsNumberToReturn, requestedItemsNumberToReturn + 1)) {
            contentItems[index].views
        }

        val promisingItems = ucb1Algorithm
            .selectMostPromisingItems(contentItems, itemsCount = requestedItemsNumberToReturn)

        promisingItems.size.shouldBe(requestedItemsNumberToReturn)
        promisingItems.shouldBe(contentItems.take(n = requestedItemsNumberToReturn).map { it.id })
    }

    @Test
    fun itemsWithHighestScoreWillBeSelectedAmongOther() {
        val contentItemsCount: Int = 7
        val requestedItemsNumberToReturn: Int = 3

        // emulate "all content already viewed"
        var contentItems = (0 until contentItemsCount)
            .map { ContentGenerator.generateValidContent().also { it.attempts++ } }

        // increase attempts for some of the items - shuffle list
        contentItems = contentItems.shuffled()
        contentItems.take(contentItemsCount - requestedItemsNumberToReturn)
            .apply { forEach { it.attempts++ } }

        log.info("Source items: ")
        contentItems.forEach { log.info(it.toString()) }

        // calculate score for every item
        contentItems.forEach {
            it.statisticalScore = updateStrategy.calculateScore(it.attempts, it.statisticalScore, 1.0)
        }

        val expectedItems = contentItems.sortedBy { it.statisticalScore }.takeLast(requestedItemsNumberToReturn)

        val promisingItems = ucb1Algorithm
            .selectMostPromisingItems(contentItems.shuffled(), itemsCount = requestedItemsNumberToReturn)

        promisingItems.size.shouldBe(requestedItemsNumberToReturn)

        log.info("Promising items:")
        promisingItems.forEach { log.info(it) }
        promisingItems.shouldContainAll(expectedItems.map { it.id })
    }

    @Test
    fun rewardWillNoDecreaseScore() {
        val content = ContentGenerator.generateValidContent()

        for (iteration in 0..10) {
            // emulate "LIKE"
            content.attempts++
            val score = updateStrategy.calculateScore(content.attempts, content.statisticalScore, 1.0)
            score.shouldBeGreaterThanOrEqual(content.statisticalScore)
            content.statisticalScore = score
        }
    }
}