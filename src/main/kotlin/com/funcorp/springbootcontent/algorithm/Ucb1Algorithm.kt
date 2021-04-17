package com.funcorp.springbootcontent.algorithm

import kotlin.math.ln
import kotlin.math.sqrt

/**
 * Upper Confidence Bound (UCB) Algorithm
 *
 * https://medium.com/analytics-vidhya/multi-armed-bandit-analysis-of-upper-confidence-bound-algorithm-4b84be516047
 * https://jeremykun.com/2013/10/28/optimism-in-the-face-of-uncertainty-the-ucb1-algorithm/
 */
class Ucb1Algorithm : BanditAlgorithm {

    constructor(numberOptionsToChoose: Int) : super(numberOptionsToChoose)

    override fun selectOption(): Int {
        var totalCount = 0

        for (index in counts.indices) {
            val count = counts[index]
            if (count == 0) {
                return index
            }
            totalCount += count
        }

        val ucbValues = DoubleArray(numberOptionsToChoose)

        for (index in 0 until numberOptionsToChoose) {
            val bonus = sqrt(2 * ln(totalCount.toDouble()) / counts[index])
            ucbValues[index] = values[index] + bonus
        }

        var maxIndex = 0

        for (index in 1 until ucbValues.size) {
            val newValue = ucbValues[index]
            if (newValue > ucbValues[maxIndex]) {
                maxIndex = index
            }
        }

        return maxIndex
    }
}