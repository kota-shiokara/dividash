package jp.ikanoshiokara.dividash.data

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingModules =
    module {
        single<SettingRepository> { SettingRepositoryImpl(androidContext().settingDataStore) }
    }
