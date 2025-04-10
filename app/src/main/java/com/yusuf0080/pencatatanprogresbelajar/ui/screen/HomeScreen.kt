package com.yusuf0080.pencatatanprogresbelajar.ui.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf0080.pencatatanprogresbelajar.R
import com.yusuf0080.pencatatanprogresbelajar.data.model.StudyLog
import com.yusuf0080.pencatatanprogresbelajar.navigation.Screen
import com.yusuf0080.pencatatanprogresbelajar.ui.components.TopBar
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

fun formatDurationFull(seconds: Int, context: android.content.Context): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes >= 60) {
        val hours = minutes / 60
        val mins = minutes % 60
        if (mins > 0) {
            context.getString(R.string.duration_hours_minutes, hours, mins)
        } else {
            context.getString(R.string.duration_hours, hours)
        }
    } else {
        context.getString(R.string.duration_minutes_seconds, minutes, remainingSeconds)
    }
}

@Composable
fun HomeScreen(navController: NavController, addLog: (StudyLog) -> Unit) {
    var time by rememberSaveable { mutableIntStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    // Pre-load string resources
    val homeTitle = stringResource(id = R.string.home_title)
    val bookOpenDesc = stringResource(id = R.string.book_open_desc)
    val bookClosedDesc = stringResource(id = R.string.book_closed_desc)
    val startLearning = stringResource(id = R.string.start_learning)
    val pauseText = stringResource(id = R.string.pause)
    val resetText = stringResource(id = R.string.reset)
    val saveSession = stringResource(id = R.string.save_session)
    val sessionSaved = stringResource(id = R.string.session_saved)
    val noDuration = stringResource(id = R.string.no_duration)
    val shareProgress = stringResource(id = R.string.share_progress)
    val viewHistory = stringResource(id = R.string.view_history)
    val viewStats = stringResource(id = R.string.view_stats)
    val shareProgressTitle = stringResource(id = R.string.share_progress_title)

    // Content descriptions
    val saveSessionDescription = stringResource(id = R.string.save_session_description)
    val shareProgressDescription = stringResource(id = R.string.share_progress_description)
    val viewHistoryDescription = stringResource(id = R.string.view_history_description)
    val viewStatsDescription = stringResource(id = R.string.view_stats_description)

    val imageRes = if (isRunning) R.drawable.terbuka else R.drawable.tertutup
    val imageDesc = if (isRunning) bookOpenDesc else bookClosedDesc

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            time++
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = homeTitle)
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = imageDesc,
                modifier = Modifier
                    .size(180.dp)
                    .semantics { contentDescription = imageDesc }
            )

            // Format duration text with context
            val formattedDuration = formatDurationFull(time, context)
            val durationText = stringResource(id = R.string.duration_format, formattedDuration)
            val durationDescription = stringResource(id = R.string.duration_description, formattedDuration)

            Text(
                text = durationText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics {
                    contentDescription = durationDescription
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { isRunning = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) {
                    Text(startLearning, color = Color.White)
                }

                Button(
                    onClick = { isRunning = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB8C00))
                ) {
                    Text(pauseText, color = Color.White)
                }

                Button(
                    onClick = {
                        isRunning = false
                        time = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text(resetText, color = Color.White)
                }
            }

            Button(
                onClick = {
                    if (time > 0) {
                        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        val log = StudyLog(
                            id = (0..9999).random(),
                            date = date,
                            durationInMinutes = time / 60
                        )
                        addLog(log)
                        Toast.makeText(context, sessionSaved, Toast.LENGTH_SHORT).show()
                        time = 0
                    } else {
                        Toast.makeText(context, noDuration, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .semantics { contentDescription = saveSessionDescription },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text(saveSession, color = Color.White)
            }

            // Create a function to get the share message
            val getShareMessage = { currentTime: Int ->
                context.getString(R.string.share_progress_message, formatDurationFull(currentTime, context))
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, getShareMessage(time))
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, shareProgressTitle))
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .semantics { contentDescription = shareProgressDescription }
            ) {
                Text(shareProgress, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(Screen.History.route) },
                    modifier = Modifier.semantics {
                        contentDescription = viewHistoryDescription
                    }
                ) {
                    Text(viewHistory, color = MaterialTheme.colorScheme.onSurface)
                }

                OutlinedButton(
                    onClick = { navController.navigate(Screen.Stats.route) },
                    modifier = Modifier.semantics {
                        contentDescription = viewStatsDescription
                    }
                ) {
                    Text(viewStats, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}