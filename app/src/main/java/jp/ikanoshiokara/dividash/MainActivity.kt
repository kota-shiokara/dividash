package jp.ikanoshiokara.dividash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.ikanoshiokara.dividash.ui.screen.main.MainScreen
import jp.ikanoshiokara.dividash.ui.screen.settings.SettingsScreen
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DividashTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(
                    LocalNavController provides navController,
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Destinations.Main,
                        ) {
                            composable<Destinations.Main> {
                                MainScreen()
                            }
                            composable<Destinations.Settings> {
                                SettingsScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

val LocalNavController =
    staticCompositionLocalOf<NavController> {
        error("No NavGraph provided")
    }

sealed interface Destinations {
    @Serializable
    data object Main : Destinations

    @Serializable
    data object Settings : Destinations
}
