package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.compose.runtime.State
import jp.ikanoshiokara.dividash.ui.UiStateHolder

interface MainUiStateHolder: UiStateHolder {
    override val uiState: MainUiState

    fun onStart()
    fun onPause()
    fun onComplete()
    fun onStop()
}
