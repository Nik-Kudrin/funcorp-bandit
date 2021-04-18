package com.funcorp.bandit.algorithm

interface ICalculateScoreStrategy {
    fun calculateScore(attempts: Int, score: Double, reward: Double): Double
}