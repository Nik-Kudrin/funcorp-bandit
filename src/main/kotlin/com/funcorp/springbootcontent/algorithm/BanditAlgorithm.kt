package com.funcorp.springbootcontent.algorithm

abstract class BanditAlgorithm {
    protected var numberOptionsToChoose = 0
    protected var counts: IntArray
    protected var values: DoubleArray

    protected var updateStrategy: IUpdateStrategy = AverageUpdateStrategy()
        set(value) {
            field = requireNotNull(updateStrategy)
        }

    constructor(numberOptionsToChoose: Int) {
        this.numberOptionsToChoose = numberOptionsToChoose
        counts = IntArray(numberOptionsToChoose)
        values = DoubleArray(numberOptionsToChoose)
    }

    abstract fun selectOption(): Int

    open fun update(arm: Int, reward: Double) {
        counts[arm]++
        values[arm] = updateStrategy.update(counts[arm], values[arm], reward)
    }

    open fun reset() {
        for (i in counts.indices) {
            counts[i] = 0
            values[i] = 0.0
        }
    }
}