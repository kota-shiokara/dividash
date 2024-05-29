package jp.ikanoshiokara.dividash.util

import org.junit.Assert.assertEquals
import org.junit.Test

class IntRangeFilterTest {
    companion object {
        const val FIRST = 0
        const val LAST = 10
        val RANGE = FIRST..LAST
        val DOWN_RANGE = LAST..FIRST
    }

    @Test
    fun `rangeFilter_範囲内`() {
        val mock = 1
        val answer = 1

        assertEquals(mock.rangeFilter(FIRST, LAST), answer)
    }

    @Test
    fun `rangeFilter_下振れ範囲外`() {
        val mock = -10
        val answer = 0

        assertEquals(mock.rangeFilter(FIRST, LAST), answer)
    }

    @Test
    fun `rangeFilter_上振れ範囲外`() {
        val mock = 100
        val answer = 10

        assertEquals(mock.rangeFilter(FIRST, LAST), answer)
    }

    @Test
    fun `rangeFilter_intRange範囲内`() {
        val mock = 1
        val answer = 1

        assertEquals(mock.rangeFilter(RANGE), answer)
    }

    @Test
    fun `rangeFilter_intRange下振れ範囲外`() {
        val mock = -10
        val answer = 0

        assertEquals(mock.rangeFilter(RANGE), answer)
    }

    @Test
    fun `rangeFilter_intRange上振れ範囲外`() {
        val mock = 100
        val answer = 10

        assertEquals(mock.rangeFilter(RANGE), answer)
    }

    @Test
    fun `rangeFilter_intRange_downTo_範囲内`() {
        val mock = 10
        val answer = 10

        assertEquals(mock.rangeFilter(DOWN_RANGE), answer)
    }

    @Test
    fun `rangeFilter_intRange_downTo_範囲外`() {
        val mock = 100
        val answer = 10

        assertEquals(mock.rangeFilter(DOWN_RANGE), answer)
    }
}
