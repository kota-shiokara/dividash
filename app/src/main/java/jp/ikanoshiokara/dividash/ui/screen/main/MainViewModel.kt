package jp.ikanoshiokara.dividash.ui.screen.main

import android.content.Context
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.SettingsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val context: Context
) : ViewModel(), MainUiStateHolder {
    override var uiState: MainUiState by mutableStateOf(MainUiState.Loading)
        private set

    override fun onStart() {
        TODO("Not yet implemented")
    }

    override fun onPause() {
        TODO("Not yet implemented")
    }

    override fun onComplete() {
        TODO("Not yet implemented")
    }

    override fun onStop() {
        TODO("Not yet implemented")
    }

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            uiState = MainUiState.Loading
            try {
                settingsRepository.userSettings.collect {
                    uiState =
                        MainUiState.Ready(
                            loading = false,
                            runningTime = it.runningTime,
                            intervalTime = it.intervalTime,
                            isAutoStart = it.isAutoStart,
                            ringtoneUri = it.ringtoneUri,
                        )
                }
            } catch (e: Exception) {
                uiState = MainUiState.Error
            }
        }
    }

    private fun checkCompleteRunning() {
        val state = uiState as? MainUiState.Ready ?: return
        if (state.isNotComplete) return

        // 音を鳴らします
        viewModelScope.launch {
            val ringtoneUri =
                if (state.ringtoneUri.isNotBlank()) {
                    state.ringtoneUri.toUri()
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

        uiState = state.copy(
            isRun = !state.isRun,
            isPlay = state.isAutoStart,
            currentTime = 0,
        )
    }

    suspend fun onRunning() {
        val state = uiState as? MainUiState.Ready ?: return

        while (state.isPlay) {
            delay(1000)
            uiState = state.copy(currentTime = state.currentTime + 1)
            checkCompleteRunning()
        }
    }
}
