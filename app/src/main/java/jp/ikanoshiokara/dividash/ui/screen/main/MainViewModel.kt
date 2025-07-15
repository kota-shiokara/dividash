package jp.ikanoshiokara.dividash.ui.screen.main

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        getSettings()
    }

    private fun getSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                settingsRepository.userSettings.collect {
                    _uiState.value =
                        MainUiState(
                            loading = false,
                            runningTime = it.runningTime,
                            intervalTime = it.intervalTime,
                            isAutoStart = it.isAutoStart,
                            ringtoneUri = it.ringtoneUri,
                        )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e) }
            }
        }
    }

    private fun checkCompleteRunning(context: Context) {
        if (_uiState.value.isNotComplete) return

        // 音を鳴らします
        viewModelScope.launch {
            val ringtoneUri =
                if (_uiState.value.ringtoneUri.isNotBlank()) {
                    _uiState.value.ringtoneUri.toUri()
                } else {
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                }

            val player =
                MediaPlayer().apply {
                    setDataSource(context, ringtoneUri)
                    isLooping = false
                    prepare()
                }
            player.start()
            delay(5000)
            player.stop()
        }

        _uiState.update {
            it.onComplete()
        }
    }

    suspend fun onRunning(context: Context) {
        while (_uiState.value.isPlay) {
            delay(1000)
            _uiState.update { it.copy(currentTime = it.currentTime + 1) }
            checkCompleteRunning(context)
        }
    }

    fun mainScreenEvent(onNavigateSetting: () -> Unit): MainScreenEvent =
        MainScreenEvent(
            onNavigateSetting = onNavigateSetting,
            onClickStartButton = {
                _uiState.update { it.onStart() }
            },
            onClickPauseButton = {
                _uiState.update { it.onPause() }
            },
            onClickStopButton = {
                _uiState.update { it.onStop() }
            },
        )
}

data class MainUiState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val runningTime: Int = -1,
    val intervalTime: Int = -1,
    val currentTime: Int = 0,
    val ringtoneUri: String = "",
    val isRun: Boolean = true,
    val isPlay: Boolean = false,
    val isAutoStart: Boolean = false,
) {
    val isInterval = !isRun
    val goalTime =
        if (isRun) {
            runningTime
        } else {
            intervalTime
        }

    val isComplete = currentTime == goalTime
    val isNotComplete = !isComplete

    fun onStart(): MainUiState = this.copy(isPlay = true)

    fun onPause(): MainUiState = this.copy(isRun = false, isPlay = false)

    fun onComplete(
        enableAutoStart: Boolean = this.isAutoStart,
    ): MainUiState =
        this.copy(
            isRun = !isRun,
            isPlay = enableAutoStart,
            currentTime = 0,
        )

    fun onStop(): MainUiState =
        this.copy(
            isRun = true,
            isPlay = false,
            currentTime = 0,
        )
}
