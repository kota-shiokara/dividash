package jp.ikanoshiokara.dividash.ui.screen

import jp.ikanoshiokara.dividash.ui.screen.main.MainViewModel
import jp.ikanoshiokara.dividash.ui.screen.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { MainViewModel(get()) }
    viewModel { SettingViewModel(get()) }
}