package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import jp.ikanoshiokara.dividash.util.formatTimer
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState()

    val runningTime by remember {
        mutableIntStateOf(uiState.value.runningTime)
    }
    val intervalTime by remember {
        mutableIntStateOf(uiState.value.intervalTime)
    }
    var currentTime by remember {
        mutableIntStateOf(0)
    }
    var isPlayTimer by remember {
        mutableStateOf(false)
    }
    var isRun by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(isPlayTimer) {
        while (isPlayTimer) {
            delay(1000)
            currentTime += 1

            if (currentTime == if (isRun) runningTime else intervalTime) {
                isPlayTimer = !isPlayTimer
                isRun = !isRun
                currentTime = 0
            }
        }
    }

    MainContent(
        goalTime = if (isRun) runningTime else intervalTime,
        currentTime = currentTime,
        isPlay = isPlayTimer,
        event = MainScreenEvent(
            onClickStartButton = {
                isPlayTimer = !isPlayTimer
            },
            onStopTimeButton = {
                currentTime = 0
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color.Red.copy(alpha = 0.8f), Color.Red.copy(alpha = 0.6f))
                    )
                ),
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
                    onClick = event.onClickStartButton,
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
                    onClick = event.onStopTimeButton,
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
    val onStopTimeButton: () -> Unit = {}
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