package jp.ikanoshiokara.dividash.ui.screen.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ikanoshiokara.dividash.ui.theme.DividashTheme
import jp.ikanoshiokara.dividash.util.formatTimer
import kotlin.math.min

private const val START_ANGLE = 270f

@Composable
fun DividashTimerCircle(
    modifier: Modifier = Modifier,
    progress: () -> Float,
    onClick: () -> Unit = {},
    indicatorColor: Color = MaterialTheme.colorScheme.primary,
    baseColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    val coercedProgress = { progress().coerceIn(0f, 1f) }

    Canvas(
        modifier =
            modifier
                .aspectRatio(1f)
                .clickable {
                    onClick()
                }
                .semantics(mergeDescendants = true) {
                    progressBarRangeInfo = ProgressBarRangeInfo(coercedProgress(), 0f..1f)
                },
    ) {
        val sweep = coercedProgress() * 360f
        val arcDimen = min(size.width, size.height)

        // base
        drawArc(
            color = baseColor,
            startAngle = START_ANGLE,
            sweepAngle = 360f,
            useCenter = true,
            size = Size(arcDimen, arcDimen),
        )

        // indicator
        drawArc(
            color = indicatorColor,
            startAngle = START_ANGLE,
            sweepAngle = sweep,
            useCenter = true,
            size = Size(arcDimen, arcDimen),
        )
    }
}

@PreviewDynamicColors
@PreviewLightDark
@Composable
fun DividashTimerCirclePreview() {
    val baseTime = 120f
    val currentTime = 80f

    DividashTheme {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            DividashTimerCircle(
                progress = {
                    currentTime / baseTime
                },
                modifier = Modifier.padding(8.dp),
            )
            Text(
                text = (baseTime - currentTime).toInt().formatTimer(),
                fontSize = 80.sp,
                letterSpacing = 8.sp,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
