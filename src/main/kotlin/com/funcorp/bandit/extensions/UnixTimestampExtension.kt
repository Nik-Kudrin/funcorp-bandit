package com.funcorp.bandit.extensions

import java.util.*

/**
 * Convert Unix timestamp to java.util.Date
 */
fun Long.toDate(): Date = Date(this * 1000)

fun String.toDate(): Date = this.toLong().toDate()
