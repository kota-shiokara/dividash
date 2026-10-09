package jp.ikanoshiokara.dividash.ui.screen.main

import jp.ikanoshiokara.dividash.ui.UiStateHolder

internal interface MainUiStateHolder : UiStateHolder {
    override val uiState: MainUiState

    fun onStart()

    fun onPause()

    fun onComplete()

    fun onStop()
}
