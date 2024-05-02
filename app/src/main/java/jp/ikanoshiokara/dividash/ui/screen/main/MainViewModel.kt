package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.SettingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingRepository: SettingRepository
): ViewModel() {
    val uiState: StateFlow<MainUiState> = combine(
        settingRepository.runningTime,
        settingRepository.intervalTime
    ) { runningTime, intervalTime ->
        MainUiState(
            runningTime = runningTime,
            intervalTime = intervalTime
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = MainUiState()
    )

    private val _uiEvents: MutableStateFlow<List<MainUiEvent>> = MutableStateFlow(emptyList())
    val uiEvents: StateFlow<List<MainUiEvent>> = _uiEvents.asStateFlow()

    // TODO: 今後ViewModelにロジックを移行する
    suspend fun onChangeTime() {

    }


    fun consumeEvent(event: MainUiEvent) {
        _uiEvents.update { e ->
            e.filterNot { it == event }
        }
    }
}

data class MainUiState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val runningTime: Int = -1,
    val intervalTime: Int = -1,
    val currentTime: Int = 0,
    val isRun: Boolean = false,
    val isInterval: Boolean = false
) {
    val isPlay = isRun || isInterval
}

sealed interface MainUiEvent {
    data class OnChangeTime(val changeTime: Int): MainUiEvent
}