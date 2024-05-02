package jp.ikanoshiokara.dividash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwi.navigationcompose.typed.Destination
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import jp.ikanoshiokara.dividash.ui.screen.main.MainScreen
import jp.ikanoshiokara.dividash.ui.screen.setting.SettingScreen
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSerializationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DividashTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalNavController provides navController
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = createRoutePattern<Destinations.Main>()
                        ) {
                            composable<Destinations.Main> {
                                MainScreen()
                            }
                            composable<Destinations.Setting> {
                                SettingScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No NavGraph provided")
}

sealed interface Destinations: Destination {
    @Serializable
    data object Main : Destinations

    @Serializable
    data object Setting : Destinations
}