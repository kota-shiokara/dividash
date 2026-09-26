package jp.ikanoshiokara.dividash.ui.screen.main

import jp.ikanoshiokara.dividash.ui.UiState

internal sealed interface MainUiState: UiState {
    data object Loading: MainUiState
    data object Error: MainUiState
    data class Ready(
        val loading: Boolean = false,
        val error: Exception? = null,
        val runningTime: Int = -1,
        val intervalTime: Int = -1,
        val currentTime: Int = 0,
        val ringtoneUri: String = "",
        val isRun: Boolean = true,
        val isPlay: Boolean = false,
        val isAutoStart: Boolean = false,
    ): MainUiState {
        val isInterval = !isRun
        val goalTime =
            if (isRun) {
                runningTime
            } else {
                intervalTime
            }

        val isComplete = currentTime == goalTime
        val isNotComplete = !isComplete

        fun onStart(): Ready = this.copy(isPlay = true)

        fun onPause(): Ready = this.copy(isRun = false, isPlay = false)

        fun onComplete(enableAutoStart: Boolean = this.isAutoStart): Ready =
            this.copy(
                isRun = !isRun,
                isPlay = enableAutoStart,
                currentTime = 0,
            )

        fun onStop(): Ready =
            this.copy(
                isRun = true,
                isPlay = false,
                currentTime = 0,
            )
    }
}
