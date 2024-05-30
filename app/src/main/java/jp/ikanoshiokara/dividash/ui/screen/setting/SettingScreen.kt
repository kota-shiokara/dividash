package jp.ikanoshiokara.dividash.ui.screen.setting

import android.util.Log
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ikanoshiokara.dividash.LocalNavController
import jp.ikanoshiokara.dividash.R
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(viewModel: SettingViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current

    SettingContent(
        runningTime = uiState.runningTime,
        intervalTime = uiState.intervalTime,
        isAutoStart = uiState.isAutoStart,
        event =
            SettingScreenEvent(
                onRunningTimeIncreaseMinutes = { viewModel.increaseRunningTime() },
                onRunningTimeDecreaseMinutes = { viewModel.decreaseRunningTime() },
                onIntervalTimeIncreaseMinutes = { viewModel.increaseIntervalTime() },
                onIntervalTimeDecreaseMinutes = { viewModel.decreaseIntervalTime() },
                onChangeAutoStart = { value ->
                    Log.d("SettingScreen", "onChangeAutoStart: $value")
                    viewModel.changeAutoStart(value)
                },
                navigateBack = {
                    navController.popBackStack()
                },
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    runningTime: Int = 25 * 60,
    intervalTime: Int = 5 * 60,
    isAutoStart: Boolean = false,
    event: SettingScreenEvent = SettingScreenEvent(),
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.setting_screen_title)) },
                navigationIcon = {
                    IconButton(onClick = event.navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            )
        },
        containerColor = MaterialTheme.colorScheme.primary,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            TimeEditRow(
                label = {
                    Text(stringResource(R.string.setting_running_time_label))
                },
                time = runningTime,
                onIncreaseMinutes = event.onRunningTimeIncreaseMinutes,
                onDecreaseMinutes = event.onRunningTimeDecreaseMinutes,
            )

            Spacer(modifier = Modifier.height(16.dp))
            TimeEditRow(
                label = {
                    Text(stringResource(R.string.setting_interval_time_label))
                },
                time = intervalTime,
                onIncreaseMinutes = event.onIntervalTimeIncreaseMinutes,
                onDecreaseMinutes = event.onIntervalTimeDecreaseMinutes,
            )

            Spacer(modifier = Modifier.height(16.dp))
            CheckBoxRow(
                checked = isAutoStart,
                onCheckedChange = event.onChangeAutoStart,
                label = {
                    Text(stringResource(R.string.setting_auto_start_label))
                },
            )
        }
    }
}

@Composable
fun TimeEditRow(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = {},
    time: Int,
    onIncreaseMinutes: () -> Unit = {},
    onDecreaseMinutes: () -> Unit = {},
) {
    SettingItemRow(
        modifier = modifier,
        label = label,
        content = {
            IconButton(onClick = onDecreaseMinutes) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${time / 60}",
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.End,
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onIncreaseMinutes) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        },
    )
}

@Composable
fun CheckBoxRow(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: @Composable () -> Unit = {},
) {
    SettingItemRow(
        modifier = modifier,
        label = {
            label()
        },
        content = {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors =
                    CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                        checkmarkColor = MaterialTheme.colorScheme.primary,
                    ),
            )
        },
    )
}

@Composable
fun SettingItemRow(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        label()
        Spacer(modifier = Modifier.weight(1f))
        content()
        Spacer(modifier = Modifier.padding(8.dp))
    }
}

data class SettingScreenEvent(
    val onRunningTimeIncreaseMinutes: () -> Unit = {},
    val onRunningTimeDecreaseMinutes: () -> Unit = {},
    val onIntervalTimeIncreaseMinutes: () -> Unit = {},
    val onIntervalTimeDecreaseMinutes: () -> Unit = {},
    val onChangeAutoStart: (Boolean) -> Unit = {},
    val navigateBack: () -> Unit = {},
)

@Preview
@Composable
fun SettingContentPreview() {
    DividashTheme {
        SettingContent()
    }
}

@Preview(locale = "ja")
@Composable
fun SettingContentJapanesePreview() {
    DividashTheme {
        SettingContent()
    }
}
