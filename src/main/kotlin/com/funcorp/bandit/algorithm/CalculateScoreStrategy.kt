package com.funcorp.bandit.algorithm

interface CalculateScoreStrategy {
    fun calculateScore(attempts: Long, score: Double, reward: Double): Double
}