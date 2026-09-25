package jp.ikanoshiokara.dividash.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatTimerTest {
    @Test
    fun `formatTimer_0秒`() {
        assertEquals("00:00", 0.formatTimer())
    }

    @Test
    fun `formatTimer_60秒未満`() {
        assertEquals("00:59", 59.formatTimer())
    }

    @Test
    fun `formatTimer_ちょうど1分`() {
        assertEquals("01:00", 60.formatTimer())
    }

    @Test
    fun `formatTimer_25分`() {
        assertEquals("25:00", (25 * 60).formatTimer())
    }

    @Test
    fun `formatTimer_100分以上`() {
        assertEquals("100:05", (100 * 60 + 5).formatTimer())
    }
}
