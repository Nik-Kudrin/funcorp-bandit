package com.funcorp.bandit.algorithm

import kotlin.math.ln
import kotlin.math.sqrt

/**
 * Upper Confidence Bound (UCB) Algorithm
 *
 * https://medium.com/analytics-vidhya/multi-armed-bandit-analysis-of-upper-confidence-bound-algorithm-4b84be516047
 * https://jeremykun.com/2013/10/28/optimism-in-the-face-of-uncertainty-the-ucb1-algorithm/
 */
class Ucb1Algorithm(totalItemsNumber: Int) : BanditAlgorithm(totalItemsNumber) {

    override fun selectTopItems(count: Int): Int {
        var totalCount = 0

        for (index in attempts.indices) {
            val attemptsCount = attempts[index]
            if (attemptsCount == 0) {
                return index
            }
            totalCount += attemptsCount
        }

        val ucbValues = DoubleArray(totalItemsNumber)

        for (index in 0 until totalItemsNumber) {
            val bonus = sqrt(2 * ln(totalCount.toDouble()) / attempts[index])
            ucbValues[index] = scores[index] + bonus
        }

        var maxIndex = 0

        for (index in 1 until ucbValues.size) {
            val currentValue = ucbValues[index]
            if (currentValue > ucbValues[maxIndex]) {
                maxIndex = index
            }
        }

        return maxIndex
    }
}