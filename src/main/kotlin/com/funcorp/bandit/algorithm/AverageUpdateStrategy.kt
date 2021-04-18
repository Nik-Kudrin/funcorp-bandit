package com.funcorp.bandit.algorithm

class AverageUpdateStrategy : ICalculateScoreStrategy {
    override fun calculateScore(attempts: Long, score: Double, reward: Double): Double {
        val score = (attempts - 1) / attempts.toDouble() * score + 1 / attempts.toDouble() * reward

        return if (score.isNaN()) 0.0
        else score
    }
}