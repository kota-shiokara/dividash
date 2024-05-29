package jp.ikanoshiokara.dividash.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.SettingRepository
import jp.ikanoshiokara.dividash.util.rangeFilter
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository,
) : ViewModel() {
    companion object {
        private const val MIN_MINUTES = 1 * 60
        private const val MAX_MINUTES = 999 * 60
    }

    val uiState =
        settingRepository.userSettings.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            SettingRepository.DEFAULT_USER_SETTINGS,
        )

    fun changeRunningTime(newRunningTime: Int) {
        viewModelScope.launch {
            settingRepository.saveRunningTime(newRunningTime.rangeFilter(MIN_MINUTES, MAX_MINUTES))
        }
    }

    fun increaseRunningTime() = changeRunningTime(uiState.value.runningTime + 60)

    fun decreaseRunningTime() = changeRunningTime(uiState.value.runningTime - 60)

    fun changeIntervalTime(newIntervalTime: Int) {
        viewModelScope.launch {
            settingRepository.saveIntervalTime(newIntervalTime.rangeFilter(MIN_MINUTES, MAX_MINUTES))
        }
    }

    fun increaseIntervalTime() = changeIntervalTime(uiState.value.intervalTime + 60)

    fun decreaseIntervalTime() = changeIntervalTime(uiState.value.intervalTime - 60)

    fun changeAutoStart(isAutoStart: Boolean) {
        viewModelScope.launch {
            settingRepository.saveAutoStart(isAutoStart)
        }
    }
}
