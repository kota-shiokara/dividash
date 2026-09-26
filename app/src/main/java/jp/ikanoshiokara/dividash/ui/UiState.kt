package jp.ikanoshiokara.dividash.ui

interface UiState {
    interface Loading : UiState

    interface Error : UiState

    interface Ready : UiState
}

interface UiStateHolder {
    val uiState: UiState
}
