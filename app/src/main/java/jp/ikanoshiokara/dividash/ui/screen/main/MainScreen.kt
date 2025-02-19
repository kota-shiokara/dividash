package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ikanoshiokara.dividash.Destinations
import jp.ikanoshiokara.dividash.LocalNavController
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import jp.ikanoshiokara.dividash.util.formatTimer
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.value.isPlay) {
        viewModel.onRunning(context)
    }

    MainContent(
        goalTime = uiState.value.goalTime,
        currentTime = uiState.value.currentTime,
        isPlay = uiState.value.isPlay,
        event =
            viewModel.mainScreenEvent(
                onNavigateSetting = {
                    navController.navigate(Destinations.Settings)
                },
            ),
    )
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    goalTime: Int = 0,
    currentTime: Int = 0,
    isPlay: Boolean = false,
    event: MainScreenEvent = MainScreenEvent(),
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(
                    onClick = event.onNavigateSetting
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        progress = { (1.0f * currentTime) / goalTime },
                        modifier =
                            Modifier
                                .aspectRatio(1f)
                                .padding(16.dp),
                        strokeWidth = 32.dp,
                        strokeCap = StrokeCap.Round,
                    )
                    Text(
                        text = (goalTime - currentTime).formatTimer(),
                        fontSize = 56.sp,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val size = 80.dp

                    OutlinedIconButton(
                        onClick = if (isPlay) event.onClickPauseButton else event.onClickStartButton,
                        modifier = Modifier.size(size),
                    ) {
                        Icon(
                            if (isPlay) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(size),
                        )
                    }
                    OutlinedIconButton(
                        onClick = event.onClickStopButton,
                        modifier = Modifier.size(size)
                    ) {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = null,
                            modifier = Modifier.size(size),
                        )
                    }
                }
            }
        }
    }
}

data class MainScreenEvent(
    val onNavigateSetting: () -> Unit = {},
    val onClickStartButton: () -> Unit = {},
    val onClickPauseButton: () -> Unit = {},
    val onClickStopButton: () -> Unit = {},
)

@PreviewScreenSizes
@PreviewLightDark
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
            goalTime = 25 * 60,
            currentTime = 2 * 60,
            isPlay = true,
        )
    }
}
