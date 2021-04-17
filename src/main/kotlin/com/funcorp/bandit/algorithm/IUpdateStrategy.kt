package com.funcorp.bandit.algorithm

interface IUpdateStrategy {
    fun update(attempts: Int, score: Double, reward: Double): Double
}