package com.yusuf0080.pencatatanprogresbelajar.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf0080.pencatatanprogresbelajar.R
import com.yusuf0080.pencatatanprogresbelajar.data.model.StudyLog
import com.yusuf0080.pencatatanprogresbelajar.navigation.Screen
import com.yusuf0080.pencatatanprogresbelajar.ui.components.TopBar

@Composable
fun HistoryScreen(navController: NavController, logs: List<StudyLog>) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    // Pre-load string resources
    val historyTitle = stringResource(id = R.string.history_title)
    val noHistory = stringResource(id = R.string.no_history)
    val noHistoryDescription = stringResource(id = R.string.no_history_description)
    val historyListTitle = stringResource(id = R.string.history_list_title)
    val historyListDescription = stringResource(id = R.string.history_list_description)
    val shareHistoryDescription = stringResource(id = R.string.share_history_description)
    val shareHistory = stringResource(id = R.string.share_history)
    val shareHistoryTitle = stringResource(id = R.string.share_history_title)
    val backButtonDescription = stringResource(id = R.string.back_button_description)
    val backToHome = stringResource(id = R.string.back_to_home)
    val getShareMessage = { totalMinutes: Int ->
        context.getString(R.string.share_history_message, totalMinutes)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = historyTitle,
                showBack = true,
                onBackClick = { navController.navigate(Screen.Home.route) }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (logs.isEmpty()) {
                Text(
                    text = noHistory,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onBackground,
                    modifier = Modifier.semantics {
                        contentDescription = noHistoryDescription
                    }
                )
            } else {
                Text(
                    text = historyListTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onBackground
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            contentDescription = historyListDescription
                        }
                ) {
                    items(logs) { log ->
                        val logText = stringResource(
                            id = R.string.date_duration_format,
                            log.date,
                            log.durationInMinutes
                        )
                        Text(
                            text = logText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val total = logs.sumOf { it.durationInMinutes }
                    val shareMessage = getShareMessage(total)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, shareMessage)
                        type = "text/plain"
                    }
                    context.startActivity(
                        Intent.createChooser(intent, shareHistoryTitle)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = shareHistoryDescription
                    },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text(
                    shareHistory,
                    color = colorScheme.onPrimary
                )
            }

            OutlinedButton(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = backButtonDescription
                    }
            ) {
                Text(
                    backToHome,
                    color = colorScheme.onSurface
                )
            }
        }
    }
}