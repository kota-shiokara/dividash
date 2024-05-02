package jp.ikanoshiokara.dividash.ui.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingContent(
        runningTime = uiState.runningTime,
        intervalTime = uiState.intervalTime,
        event = SettingScreenEvent(
            onRunningTimeIncreaseMinutes = { viewModel.changeRunningTime(uiState.runningTime + 1) },
            onRunningTimeDecreaseMinutes = { viewModel.changeRunningTime(uiState.runningTime - 1) },
            onRunningTimeValueChange = { value -> viewModel.changeRunningTime(value) }
        )
    )
}

@Composable
fun SettingContent(
    runningTime: Int = 25,
    intervalTime: Int = 5,
    event: SettingScreenEvent = SettingScreenEvent()
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            TimeEditRow(
                time = runningTime,
                onIncreaseMinutes = event.onRunningTimeIncreaseMinutes,
                onDecreaseMinutes = event.onRunningTimeDecreaseMinutes,
                onValueChange = { value -> event.onRunningTimeValueChange(value) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            TimeEditRow(
                time = intervalTime
            )
        }
    }
}

@Composable
fun TimeEditRow(
    modifier: Modifier = Modifier,
    time: Int,
    onIncreaseMinutes: () -> Unit = {},
    onDecreaseMinutes: () -> Unit = {},
    onValueChange: (Int) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedIconButton(onClick = onDecreaseMinutes) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
        }
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = if (time != 0) "$time" else "",
            onValueChange = { value ->
                val newValue = (value.toIntOrNull() ?: 0)
                onValueChange(newValue)
            },
            placeholder = {
                if (time == 0) { Text(text = "0") }
            },
            modifier = Modifier.width(64.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedIconButton(onClick = onIncreaseMinutes) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

data class SettingScreenEvent(
    val onRunningTimeIncreaseMinutes: () -> Unit = {},
    val onRunningTimeDecreaseMinutes: () -> Unit = {},
    val onRunningTimeValueChange: (Int) -> Unit = {}
)

@Preview
@Composable
fun SettingContentPreview() {
    DividashTheme {
        SettingContent()
    }
}