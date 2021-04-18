package com.funcorp.bandit.statistics.model

interface IBanditScorable<T> {
    val id: T

    /**
     * How many times items was accessed
     */
    var attempts: Long

    /**
     * Calculated score for item
     */
    var statisticalScore: Double
}