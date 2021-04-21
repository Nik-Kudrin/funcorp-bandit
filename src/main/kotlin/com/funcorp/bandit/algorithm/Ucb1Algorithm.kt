package com.funcorp.bandit.algorithm

import com.funcorp.bandit.statistics.model.BanditScorable
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Upper Confidence Bound (UCB) Algorithm
 *
 * https://medium.com/analytics-vidhya/multi-armed-bandit-analysis-of-upper-confidence-bound-algorithm-4b84be516047
 * https://jeremykun.com/2013/10/28/optimism-in-the-face-of-uncertainty-the-ucb1-algorithm/
 */
class Ucb1Algorithm : BanditAlgorithm {

    override fun <T> selectMostPromisingItems(items: List<BanditScorable<T>>, itemsCount: Int): List<T> {
        var totalAttemptsCount = 0L

        val notExploredItems = items.filter { it.attempts == 0L }
        if (notExploredItems.size >= itemsCount)
            return notExploredItems.take(itemsCount).map { it.id }

        totalAttemptsCount = items.sumOf { it.attempts }
        val attemptsLogarithm = 2 * ln(totalAttemptsCount.toDouble())

        val ucbAlgorithmScores = items.map {
            val bonus = sqrt(attemptsLogarithm / it.attempts)
            Pair(it.id, it.statisticalScore + bonus)
        }

        val promisingItems = ucbAlgorithmScores
            .sortedBy { it.second }
            .takeLast(itemsCount)
            .map { it.first }

        // if have not enough NOT explored items
        return notExploredItems
            .map { it.id }
            .toMutableList()
            // add promising items to end of list of not explored
            .also { it.addAll(promisingItems) }
            .take(min(items.size, itemsCount))
    }
}