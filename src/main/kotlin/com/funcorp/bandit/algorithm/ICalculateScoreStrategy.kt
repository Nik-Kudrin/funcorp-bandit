package com.funcorp.bandit.algorithm

interface ICalculateScoreStrategy {
    fun calculateScore(attempts: Long, score: Double, reward: Double): Double
}