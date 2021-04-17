package com.funcorp.bandit.algorithm

abstract class BanditAlgorithm {
    protected var totalItemsNumber = 0
    protected var attempts: IntArray
    protected var scores: DoubleArray

    protected var updateStrategy: IUpdateStrategy = AverageUpdateStrategy()
        set(value) {
            field = requireNotNull(value)
        }

    constructor(totalItemsNumber: Int) {
        this.totalItemsNumber = totalItemsNumber
        attempts = IntArray(totalItemsNumber)
        scores = DoubleArray(totalItemsNumber)
    }

    abstract fun selectTopItems(count: Int = 1): Int

    open fun update(itemIndex: Int, reward: Double) {
        attempts[itemIndex]++
        scores[itemIndex] = updateStrategy.update(attempts[itemIndex], scores[itemIndex], reward)
    }
}