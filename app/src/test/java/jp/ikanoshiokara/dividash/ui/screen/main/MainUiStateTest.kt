package jp.ikanoshiokara.dividash.ui.screen.main

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MainUiStateTest {
    companion object {
        const val RUNNING_TIME = 1500
        const val INTERVAL_TIME = 300
    }

    private val initialState =
        MainUiState(
            runningTime = RUNNING_TIME,
            intervalTime = INTERVAL_TIME,
        )

    @Test
    fun `goalTime_作業中はrunningTime`() {
        assertEquals(RUNNING_TIME, initialState.goalTime)
    }

    @Test
    fun `goalTime_休憩中はintervalTime`() {
        val state = initialState.copy(isRun = false)

        assertTrue(state.isInterval)
        assertEquals(INTERVAL_TIME, state.goalTime)
    }

    @Test
    fun `isComplete_goalTimeに達したら完了`() {
        assertFalse(initialState.copy(currentTime = RUNNING_TIME - 1).isComplete)
        assertTrue(initialState.copy(currentTime = RUNNING_TIME).isComplete)
    }

    @Test
    fun `onStart_再生状態になる`() {
        assertTrue(initialState.onStart().isPlay)
    }

    @Test
    fun `onComplete_作業から休憩に切り替わり時間がリセットされる`() {
        val state =
            initialState
                .copy(currentTime = RUNNING_TIME, isPlay = true)
                .onComplete()

        assertFalse(state.isRun)
        assertEquals(0, state.currentTime)
        assertFalse(state.isPlay)
    }

    @Test
    fun `onComplete_自動開始が有効なら再生を続ける`() {
        val state =
            initialState
                .copy(currentTime = RUNNING_TIME, isPlay = true, isAutoStart = true)
                .onComplete()

        assertTrue(state.isPlay)
    }

    @Test
    fun `onStop_作業の最初に戻る`() {
        val state =
            initialState
                .copy(isRun = false, isPlay = true, currentTime = 100)
                .onStop()

        assertTrue(state.isRun)
        assertFalse(state.isPlay)
        assertEquals(0, state.currentTime)
    }
}
