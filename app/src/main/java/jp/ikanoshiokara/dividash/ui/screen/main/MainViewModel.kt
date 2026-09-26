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
    private val context: Context,
) : ViewModel(),
    MainUiStateHolder {
    override var uiState: MainUiState by mutableStateOf(MainUiState.Loading)
        private set

    override fun onStart() {
        updateReady { it.onStart() }
    }

    override fun onPause() {
        updateReady { it.onPause() }
    }

    override fun onComplete() {
        updateReady { it.onComplete() }
    }

    override fun onStop() {
        updateReady { it.onStop() }
    }

    init {
        load()
    }

    private inline fun updateReady(transform: (MainUiState.Ready) -> MainUiState.Ready) {
        val state = uiState as? MainUiState.Ready ?: return
        uiState = transform(state)
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

        onComplete()
    }

    suspend fun onRunning() {
        // 毎周期 uiState を読み直し、一時停止・停止・ラップ切り替えを反映する
        while ((uiState as? MainUiState.Ready)?.isPlay == true) {
            delay(1000)
            updateReady { it.copy(currentTime = it.currentTime + 1) }
            checkCompleteRunning()
        }
    }
}
