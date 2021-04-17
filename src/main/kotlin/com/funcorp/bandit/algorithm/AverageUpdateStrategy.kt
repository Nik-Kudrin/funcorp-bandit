package com.funcorp.bandit.algorithm

class AverageUpdateStrategy : IUpdateStrategy {
    override fun update(attempts: Int, score: Double, reward: Double): Double {
        return (attempts - 1) / attempts.toDouble() * score + 1 / attempts.toDouble() * reward
    }
}