package com.funcorp.bandit.algorithm

class AverageUpdateStrategy : ICalculateScoreStrategy {
    override fun calculateScore(attempts: Int, score: Double, reward: Double): Double =
        (attempts - 1) / attempts.toDouble() * score + 1 / attempts.toDouble() * reward
}