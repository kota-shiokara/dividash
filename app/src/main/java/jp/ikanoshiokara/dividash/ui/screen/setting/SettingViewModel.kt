package jp.ikanoshiokara.dividash.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.SettingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingViewModel(
    private val settingRepository: SettingRepository
): ViewModel() {
    val uiState: StateFlow<SettingUiState> = combine(
        settingRepository.runningTime,
        settingRepository.intervalTime
    ) { runningTime, intervalTime ->
        SettingUiState(
            runningTime = runningTime,
            intervalTime = intervalTime
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = SettingUiState(loading = true)
    )

    fun changeRunningTime(newRunningTime: Int) {
        viewModelScope.launch {
            settingRepository.saveRunningTime(newRunningTime)
        }
    }
}

data class SettingUiState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val runningTime: Int = -1,
    val intervalTime: Int = -1
) {
    val showContent = !loading && error == null
}

