package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jp.ikanoshiokara.dividash.data.SettingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingRepository: SettingRepository,
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
                settingRepository.userSettings.collect {
                    _uiState.value =
                        MainUiState(
                            loading = false,
                            runningTime = it.runningTime,
                            intervalTime = it.intervalTime,
                            isAutoStart = it.isAutoStart,
                        )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e) }
            }
        }
    }

    private fun checkCompleteRunning() {
        if (_uiState.value.isNotComplete) return
        _uiState.update {
            if (_uiState.value.isAutoStart) {
                it.onAutoStart()
            } else {
                it.onComplete()
            }
        }
    }

    suspend fun onRunning() {
        while (_uiState.value.isPlay) {
            delay(1000)
            _uiState.update { it.copy(currentTime = it.currentTime + 1) }
            checkCompleteRunning()
        }
    }

    fun mainScreenEvent(onNavigateSetting: () -> Unit): MainScreenEvent {
        return MainScreenEvent(
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
}

data class MainUiState(
    val loading: Boolean = false,
    val error: Exception? = null,
    val runningTime: Int = -1,
    val intervalTime: Int = -1,
    val currentTime: Int = 0,
    val isRun: Boolean = false,
    val isInterval: Boolean = false,
    val isAutoStart: Boolean = false,
    val prevDivisionType: DivisionType = DivisionType.Interval,
    val currentDivisionType: DivisionType = DivisionType.Running,
) {
    val isPlay = isRun || isInterval
    val goalTime =
        if (currentDivisionType == DivisionType.Running) {
            runningTime
        } else {
            intervalTime
        }

    val isComplete = currentTime == goalTime
    val isNotComplete = !isComplete

    fun onStart(): MainUiState {
        return if (prevDivisionType == DivisionType.Interval) {
            this.copy(isRun = true, currentDivisionType = DivisionType.Running)
        } else {
            this.copy(isInterval = true, currentDivisionType = DivisionType.Interval)
        }
    }

    fun onAutoStart(): MainUiState {
        val nextDivisionType =
            if (currentDivisionType == DivisionType.Interval) {
                DivisionType.Running
            } else {
                DivisionType.Interval
            }
        return this.copy(
            isRun = nextDivisionType == DivisionType.Running,
            isInterval = nextDivisionType == DivisionType.Interval,
            currentTime = 0,
            prevDivisionType = currentDivisionType,
            currentDivisionType = nextDivisionType,
        )
    }

    fun onPause(): MainUiState {
        return this.copy(isRun = false, isInterval = false)
    }

    fun onComplete(): MainUiState {
        return this.copy(
            isRun = false,
            isInterval = false,
            currentTime = 0,
            prevDivisionType = currentDivisionType,
            currentDivisionType =
                if (currentDivisionType == DivisionType.Interval) {
                    DivisionType.Running
                } else {
                    DivisionType.Interval
                },
        )
    }

    fun onStop(): MainUiState {
        return this.copy(
            isRun = false,
            isInterval = false,
            currentTime = 0,
            currentDivisionType = DivisionType.Running,
        )
    }

    sealed interface DivisionType {
        data object Running : DivisionType

        data object Interval : DivisionType
    }
}
