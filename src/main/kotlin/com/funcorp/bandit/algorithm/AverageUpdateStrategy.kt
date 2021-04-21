package com.funcorp.bandit.algorithm

class AverageUpdateStrategy : CalculateScoreStrategy {
    override fun calculateScore(attempts: Long, score: Double, reward: Double): Double {
        val calculatedScore = (attempts - 1) / attempts.toDouble() * score + 1 / attempts.toDouble() * reward

        return if (calculatedScore.isNaN()) 0.0
        else calculatedScore
    }
}