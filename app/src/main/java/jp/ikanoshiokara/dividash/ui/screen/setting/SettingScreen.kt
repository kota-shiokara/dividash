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
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.ikanoshiokara.dividash.LocalNavController
import jp.ikanoshiokara.dividash.R
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = LocalNavController.current

    SettingContent(
        runningTime = uiState.runningTime,
        intervalTime = uiState.intervalTime,
        event = SettingScreenEvent(
            onRunningTimeIncreaseMinutes = { viewModel.increaseRunningTime() },
            onRunningTimeDecreaseMinutes = { viewModel.decreaseRunningTime() },
            onRunningTimeValueChange = { value -> viewModel.changeRunningTime(value) },
            onIntervalTimeIncreaseMinutes = { viewModel.increaseIntervalTime() },
            onIntervalTimeDecreaseMinutes = { viewModel.decreaseIntervalTime() },
            onIntervalTimeValueChange = { value -> viewModel.changeIntervalTime(value) },
            onChangeAutoStart = { value -> viewModel.changeAutoStart(value) },
            navigateBack = {
                navController.popBackStack()
            }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingContent(
    runningTime: Int = 25,
    intervalTime: Int = 5,
    isAutoStart: Boolean = false,
    event: SettingScreenEvent = SettingScreenEvent()
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(stringResource(R.string.setting_running_time_label))
            Spacer(modifier = Modifier.height(16.dp))
            TimeEditRow(
                time = runningTime,
                onIncreaseMinutes = event.onRunningTimeIncreaseMinutes,
                onDecreaseMinutes = event.onRunningTimeDecreaseMinutes,
                onValueChange = { value -> event.onRunningTimeValueChange(value) }
            )

            Spacer(modifier = Modifier.height(32.dp))
            Text(stringResource(R.string.setting_interval_time_label))
            Spacer(modifier = Modifier.height(16.dp))
            TimeEditRow(
                time = intervalTime,
                onIncreaseMinutes = event.onIntervalTimeIncreaseMinutes,
                onDecreaseMinutes = event.onIntervalTimeDecreaseMinutes,
                onValueChange = { value -> event.onIntervalTimeValueChange(value) }
            )

            Spacer(modifier = Modifier.height(32.dp))
            CheckBoxRow(
                checked = isAutoStart,
                onCheckedChange = event.onChangeAutoStart
            ) {
                Text(stringResource(R.string.setting_auto_start_label))
            }
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
            value = if (time != 0) "${time / 60}" else "",
            onValueChange = { value ->
                val newValue = (value.toIntOrNull() ?: 0) * 60
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
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedIconButton(onClick = onIncreaseMinutes) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
        }
    }
}

@Composable
fun CheckBoxRow(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    content: @Composable () -> Unit = {}
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        content()
    }
}

data class SettingScreenEvent(
    val onRunningTimeIncreaseMinutes: () -> Unit = {},
    val onRunningTimeDecreaseMinutes: () -> Unit = {},
    val onRunningTimeValueChange: (Int) -> Unit = {},
    val onIntervalTimeIncreaseMinutes: () -> Unit = {},
    val onIntervalTimeDecreaseMinutes: () -> Unit = {},
    val onIntervalTimeValueChange: (Int) -> Unit = {},
    val onChangeAutoStart: (Boolean) -> Unit = {},
    val navigateBack: () -> Unit = {}
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