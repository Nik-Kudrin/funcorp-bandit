package com.funcorp.bandit.algorithm

abstract class BanditAlgorithm {
    protected var totalItemsNumber = 0
    protected var counts: IntArray
    protected var values: DoubleArray

    protected var updateStrategy: IUpdateStrategy = AverageUpdateStrategy()
        set(value) {
            field = requireNotNull(updateStrategy)
        }

    constructor(totalItemsNumber: Int) {
        this.totalItemsNumber = totalItemsNumber
        counts = IntArray(totalItemsNumber)
        values = DoubleArray(totalItemsNumber)
    }

    abstract fun selectOption(): Int

    open fun update(itemIndex: Int, reward: Double) {
        counts[itemIndex]++
        values[itemIndex] = updateStrategy.update(counts[itemIndex], values[itemIndex], reward)
    }

    open fun reset() {
        for (i in counts.indices) {
            counts[i] = 0
            values[i] = 0.0
        }
    }
}