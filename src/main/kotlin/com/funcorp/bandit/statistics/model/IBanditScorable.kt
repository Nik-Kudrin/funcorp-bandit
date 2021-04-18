package com.funcorp.bandit.statistics.model

interface IBanditScorable<T> {
    val id: T

    /**
     * How many times items was accessed
     */
    var attempts: Int

    /**
     * Calculated score for item
     */
    var statisticalScore: Double
}