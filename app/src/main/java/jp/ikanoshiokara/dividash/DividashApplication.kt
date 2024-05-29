package jp.ikanoshiokara.dividash

import android.app.Application
import jp.ikanoshiokara.dividash.data.settingModules
import jp.ikanoshiokara.dividash.ui.screen.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DividashApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@DividashApplication)
            modules(
                settingModules,
                viewModelModules,
            )
        }
    }
}
