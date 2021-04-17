package com.funcorp.bandit.algorithm

class AverageUpdateStrategy : IUpdateStrategy {
    override fun update(numberOptionsToChoose: Int, value: Double, reward: Double): Double {
        return (numberOptionsToChoose - 1) / numberOptionsToChoose.toDouble() * value + 1 / numberOptionsToChoose.toDouble() * reward
    }
}