package com.funcorp.springbootcontent.algorithm

interface IUpdateStrategy {
    fun update(numberOptionsToChoose: Int, value: Double, reward: Double): Double
}