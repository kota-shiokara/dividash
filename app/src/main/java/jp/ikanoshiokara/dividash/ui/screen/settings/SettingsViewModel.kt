package jp.ikanoshiokara.dividash.ui.screen.settings

import android.content.Context
import android.media.RingtoneManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.RingtoneInfo
import jp.ikanoshiokara.dividash.data.SettingsRepository
import jp.ikanoshiokara.dividash.data.UserSettings
import jp.ikanoshiokara.dividash.ui.screen.main.MainUiState
import jp.ikanoshiokara.dividash.util.rangeFilter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    companion object {
        private const val MIN_MINUTES = 1 * 60
        private const val MAX_MINUTES = 999 * 60
    }

    private val _uiState: MutableStateFlow<SettingUiState> = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    fun init(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            try {
                val ringtoneList = settingsRepository.getRingtoneList(context)
                settingsRepository.userSettings.collect {
                    _uiState.value =
                        SettingUiState.fromUserSettings(it).copy(
                            loading = false,
                            ringtoneList = ringtoneList
                        )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e) }
            }
        }
    }

    fun changeRunningTime(newRunningTime: Int) {
        viewModelScope.launch {
            settingsRepository.saveRunningTime(newRunningTime.rangeFilter(MIN_MINUTES, MAX_MINUTES))
        }
    }

    fun increaseRunningTime() = changeRunningTime(uiState.value.runningTime + 60)

    fun decreaseRunningTime() = changeRunningTime(uiState.value.runningTime - 60)

    fun changeIntervalTime(newIntervalTime: Int) {
        viewModelScope.launch {
            settingsRepository.saveIntervalTime(newIntervalTime.rangeFilter(MIN_MINUTES, MAX_MINUTES))
        }
    }

    fun increaseIntervalTime() = changeIntervalTime(uiState.value.intervalTime + 60)

    fun decreaseIntervalTime() = changeIntervalTime(uiState.value.intervalTime - 60)

    fun changeAutoStart(isAutoStart: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveAutoStart(isAutoStart)
        }
    }

    fun changeRingtoneType(newRingtoneType: Int) {
        viewModelScope.launch {
            settingsRepository.saveRingtoneType(newRingtoneType)
        }
    }
}

data class SettingUiState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val runningTime: Int = -1,
    val intervalTime: Int = -1,
    val isAutoStart: Boolean = false,
    val ringtoneType: Int = -1,
    val ringtoneList: List<RingtoneInfo> = emptyList(),
) {
    companion object {
        fun fromUserSettings(settings: UserSettings): SettingUiState {
            return SettingUiState(
                runningTime = settings.runningTime,
                intervalTime = settings.intervalTime,
                isAutoStart = settings.isAutoStart,
                ringtoneType = settings.ringtoneType
            )
        }
    }
}
