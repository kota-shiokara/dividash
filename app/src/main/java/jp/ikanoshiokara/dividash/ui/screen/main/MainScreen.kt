package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kiwi.navigationcompose.typed.navigate
import jp.ikanoshiokara.dividash.Destinations
import jp.ikanoshiokara.dividash.LocalNavController
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import jp.ikanoshiokara.dividash.util.formatTimer
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.value.isPlay) {
        viewModel.onRunning()
    }

    MainContent(
        goalTime = if (uiState.value.isRun) uiState.value.runningTime else uiState.value.intervalTime,
        currentTime = uiState.value.currentTime,
        isPlay = uiState.value.isPlay,
        event = viewModel.mainScreenEvent(
            onNavigateSetting = {
                navController.navigate(Destinations.Setting)
            }
        )
    )
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    goalTime: Int = 0,
    currentTime: Int = 0,
    isPlay: Boolean = false,
    event: MainScreenEvent = MainScreenEvent()
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = event.onNavigateSetting,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                }
            }
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { (1.0f * currentTime) / goalTime },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(16.dp),
                    color = Color.White,
                    strokeWidth = 32.dp,
                    trackColor = Color.White.copy(alpha = 0.4f),
                    strokeCap = StrokeCap.Round
                )
                Text(
                    text = (goalTime - currentTime).formatTimer(),
                    color = Color.White,
                    fontSize = 56.sp
                )
            }
            Row {
                val size = 100.dp

                IconButton(
                    onClick = if (isPlay) event.onClickPauseButton else event.onClickStartButton,
                    modifier = Modifier.size(size),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        if (isPlay) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(size)
                    )
                }
                IconButton(
                    onClick = event.onClickStopButton,
                    modifier = Modifier.size(size),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(size)
                    )
                }
            }
        }
    }
}

data class MainScreenEvent(
    val onNavigateSetting: () -> Unit = {},
    val onClickStartButton: () -> Unit = {},
    val onClickPauseButton: () -> Unit = {},
    val onClickStopButton: () -> Unit = {}
)

@Preview
@Composable
fun MainScreenDefaultPreview() {
    DividashTheme {
        MainContent()
    }
}

@Preview
@Composable
fun MainScreenIsPlayPreview() {
    DividashTheme {
        MainContent(
            goalTime = 5 * 60,
            currentTime = 150,
            isPlay = true
        )
    }
}