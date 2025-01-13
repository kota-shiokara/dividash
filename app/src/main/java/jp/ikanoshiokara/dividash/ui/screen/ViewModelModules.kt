package jp.ikanoshiokara.dividash.ui.screen

import jp.ikanoshiokara.dividash.ui.screen.main.MainViewModel
import jp.ikanoshiokara.dividash.ui.screen.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModules =
    module {
        viewModel { MainViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
    }
