package com.funcorp.bandit.statistics.model

interface BanditScorable<T> {
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