package com.funcorp.bandit.algorithm

import com.funcorp.bandit.statistics.model.IBanditScorable

interface BanditAlgorithm {
    fun <T> selectMostPromisingItems(items: List<IBanditScorable<T>>, itemsCount: Int = 1): List<T>
}