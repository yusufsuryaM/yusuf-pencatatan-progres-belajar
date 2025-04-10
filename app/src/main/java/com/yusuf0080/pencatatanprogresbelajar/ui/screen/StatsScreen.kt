package com.yusuf0080.pencatatanprogresbelajar.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yusuf0080.pencatatanprogresbelajar.R
import com.yusuf0080.pencatatanprogresbelajar.data.model.StudyLog
import com.yusuf0080.pencatatanprogresbelajar.navigation.Screen
import com.yusuf0080.pencatatanprogresbelajar.ui.components.TopBar
import com.yusuf0080.pencatatanprogresbelajar.viewmodel.StatsViewModel

@SuppressLint("DefaultLocale")
@Composable
fun StatsScreen(
    navController: NavController,
    logs: List<StudyLog>,
    viewModel: StatsViewModel = viewModel()
) {
    val weeklyTarget = viewModel.weeklyTarget
    val dailyTarget = viewModel.dailyTarget
    val colorScheme = MaterialTheme.colorScheme

    val total = logs.sumOf { it.durationInMinutes }
    val progressWeekly = total.toFloat() / weeklyTarget.toFloat()
    val progressDaily = total.toFloat() / dailyTarget.toFloat()

    // Pre-load string resources
    val statsTitle = stringResource(R.string.stats_title)
    val weeklyProgressText = stringResource(R.string.weekly_progress)
    val dailyProgressText = stringResource(R.string.daily_progress)
    val weeklyTargetLabel = stringResource(R.string.weekly_target_label)
    val dailyTargetLabel = stringResource(R.string.daily_target_label)
    val backToHomeText = stringResource(R.string.back_to_home)
    val backButtonDesc = stringResource(R.string.back_button_description)
    val viewStatsDesc = stringResource(R.string.view_stats_description)
    val minutesText = stringResource(R.string.minutes)

    // Pre-load duration string formats
    val durationHoursMinutesFormat = stringResource(R.string.duration_hours_minutes)
    val durationHoursFormat = stringResource(R.string.duration_hours)

    // Format the durations using the pre-loaded strings
    val formattedTotalDuration = rememberSaveable (total) {
        formatDuration(total, minutesText, durationHoursFormat, durationHoursMinutesFormat)
    }

    val formattedWeeklyTarget = rememberSaveable (weeklyTarget) {
        formatDuration(weeklyTarget, minutesText, durationHoursFormat, durationHoursMinutesFormat)
    }

    val formattedDailyTarget = rememberSaveable (dailyTarget) {
        formatDuration(dailyTarget, minutesText, durationHoursFormat, durationHoursMinutesFormat)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = statsTitle,
                showBack = true,
                onBackClick = { navController.navigate(Screen.Home.route) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .semantics { contentDescription = viewStatsDesc },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Weekly progress section
            Text(
                text = weeklyProgressText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )

            val weeklyProgressDesc = remember(progressWeekly) {
                "$weeklyProgressText: ${(progressWeekly * 100).toInt()}%"
            }

            LinearProgressIndicator(
                progress = { progressWeekly.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .semantics { contentDescription = weeklyProgressDesc },
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceVariant
            )

            Text(
                text = "$formattedTotalDuration / $formattedWeeklyTarget",
                color = colorScheme.onSurface
            )

            OutlinedTextField(
                value = weeklyTarget.toString(),
                onValueChange = { it.toIntOrNull()?.let { value -> viewModel.weeklyTarget = value } },
                label = { Text(weeklyTargetLabel) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = weeklyTargetLabel },
                singleLine = true
            )

            HorizontalDivider(color = colorScheme.outline)

            // Daily progress section
            Text(
                text = dailyProgressText,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )

            val dailyProgressDesc = remember(progressDaily) {
                "$dailyProgressText: ${(progressDaily * 100).toInt()}%"
            }

            LinearProgressIndicator(
                progress = { progressDaily.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .semantics { contentDescription = dailyProgressDesc },
                color = colorScheme.tertiary,
                trackColor = colorScheme.surfaceVariant
            )

            Text(
                text = "$formattedTotalDuration / $formattedDailyTarget",
                color = colorScheme.onSurface
            )

            OutlinedTextField(
                value = dailyTarget.toString(),
                onValueChange = { it.toIntOrNull()?.let { value -> viewModel.dailyTarget = value } },
                label = { Text(dailyTargetLabel) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = dailyTargetLabel },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Back button
            OutlinedButton(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = backButtonDesc }
            ) {
                Text(
                    text = backToHomeText,
                    color = colorScheme.onSurface
                )
            }
        }
    }
}

private fun formatDuration(
    minutes: Int,
    minutesText: String,
    hoursFormat: String,
    hoursMinutesFormat: String
): String {
    return if (minutes >= 60) {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        if (remainingMinutes > 0) {
            String.format(hoursMinutesFormat, hours, remainingMinutes)
        } else {
            String.format(hoursFormat, hours)
        }
    } else {
        "$minutes $minutesText"
    }
}