package jp.ikanoshiokara.dividash.util

import kotlin.math.max
import kotlin.math.min

fun Int.rangeFilter(from: Int, to: Int): Int {
    val min = min(from, to)
    val max = max(from, to)
    if (this in (min..max)) return this
    return if (max < this) max else min
}

fun Int.rangeFilter(intRange: IntRange): Int {
    return this.rangeFilter(intRange.first, intRange.last)
}