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

    fun formatDuration(minutes: Int): String {
        return if (minutes >= 60) {
            val hours = minutes / 60
            val remainingMinutes = minutes % 60
            if (remainingMinutes > 0) "$hours jam $remainingMinutes menit"
            else "$hours jam"
        } else {
            "$minutes menit"
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.stats_title),
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
                .semantics { contentDescription = "Halaman statistik belajar" },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.weekly_progress),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            LinearProgressIndicator(
                progress = { progressWeekly.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .semantics {
                        contentDescription = "Progress mingguan: ${(progressWeekly * 100).toInt()} persen"
                    },
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceVariant
            )
            Text(
                text = "${formatDuration(total)} / ${formatDuration(weeklyTarget)}",
                color = colorScheme.onSurface
            )

            OutlinedTextField(
                value = weeklyTarget.toString(),
                onValueChange = { it.toIntOrNull()?.let { value -> viewModel.weeklyTarget = value } },
                label = { Text(stringResource(R.string.weekly_target_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Input target menit belajar mingguan" },
                singleLine = true
            )

            HorizontalDivider(color = colorScheme.outline)

            Text(
                text = stringResource(R.string.daily_progress),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )
            LinearProgressIndicator(
                progress = { progressDaily.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .semantics {
                        contentDescription = "Progress harian: ${(progressDaily * 100).toInt()} persen"
                    },
                color = colorScheme.tertiary,
                trackColor = colorScheme.surfaceVariant
            )
            Text(
                text = "${formatDuration(total)} / ${formatDuration(dailyTarget)}",
                color = colorScheme.onSurface
            )

            OutlinedTextField(
                value = dailyTarget.toString(),
                onValueChange = { it.toIntOrNull()?.let { value -> viewModel.dailyTarget = value } },
                label = { Text(stringResource(R.string.daily_target_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Input target menit belajar harian" },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Tombol kembali ke halaman utama" }
            ) {
                Text(
                    text = stringResource(R.string.back_to_home),
                    color = colorScheme.onSurface
                )
            }
        }
    }
}