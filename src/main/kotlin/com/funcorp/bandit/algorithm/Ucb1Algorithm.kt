package com.funcorp.bandit.algorithm

import com.funcorp.bandit.statistics.model.IBanditScorable
import kotlin.math.ln
import kotlin.math.sqrt

/**
 * Upper Confidence Bound (UCB) Algorithm
 *
 * https://medium.com/analytics-vidhya/multi-armed-bandit-analysis-of-upper-confidence-bound-algorithm-4b84be516047
 * https://jeremykun.com/2013/10/28/optimism-in-the-face-of-uncertainty-the-ucb1-algorithm/
 */
class Ucb1Algorithm : BanditAlgorithm {

    override fun <T> selectMostPromisingItems(items: List<IBanditScorable<T>>, itemsCount: Int): List<T> {
        var totalAttemptsCount = 0

        val notExploredItems = items.filter { it.attempts == 0 }

        if (notExploredItems.size >= itemsCount) return notExploredItems.map { it.id }

        totalAttemptsCount += items.sumBy { it.attempts }
        val remainingItems = itemsCount - notExploredItems.size

        val ucbAlgorithmScores = items.map {
            val bonus = sqrt(2 * ln(totalAttemptsCount.toDouble()) / it.attempts)
            Pair(it.id, it.statisticalScore + bonus)
        }

        return ucbAlgorithmScores
            .sortedBy { it.second }
            .takeLast(remainingItems)
            .map { it.first }
    }
}