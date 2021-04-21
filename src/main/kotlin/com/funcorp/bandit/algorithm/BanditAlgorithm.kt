package com.funcorp.bandit.algorithm

import com.funcorp.bandit.statistics.model.BanditScorable

interface BanditAlgorithm {
    fun <T> selectMostPromisingItems(items: List<BanditScorable<T>>, itemsCount: Int = 1): List<T>
}