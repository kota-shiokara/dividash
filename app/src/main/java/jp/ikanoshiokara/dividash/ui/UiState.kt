package jp.ikanoshiokara.dividash.ui

import androidx.compose.runtime.State

interface UiState {
    interface Loading : UiState
    interface Error : UiState
    interface Ready : UiState
}

interface UiStateHolder {
    val uiState: State<UiState>
}
