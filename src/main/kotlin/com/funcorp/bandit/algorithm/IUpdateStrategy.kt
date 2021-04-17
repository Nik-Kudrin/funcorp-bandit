package com.funcorp.bandit.algorithm

interface IUpdateStrategy {
    fun update(numberOptionsToChoose: Int, value: Double, reward: Double): Double
}