package com.funcorp.bandit.algorithm

interface ScoreStrategy {
    fun calculateScore(attempts: Long, score: Double, reward: Double): Double
}