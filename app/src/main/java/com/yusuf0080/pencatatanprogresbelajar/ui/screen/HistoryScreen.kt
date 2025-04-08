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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf0080.pencatatanprogresbelajar.data.model.StudyLog
import com.yusuf0080.pencatatanprogresbelajar.navigation.Screen
import com.yusuf0080.pencatatanprogresbelajar.ui.components.TopBar

@Composable
fun HistoryScreen(navController: NavController, logs: List<StudyLog>) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            TopBar(
                title = "Riwayat Belajar",
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
                    text = "Belum ada riwayat belajar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onBackground,
                    modifier = Modifier.semantics {
                        contentDescription = "Belum ada data riwayat belajar yang tersedia"
                    }
                )
            } else {
                Text(
                    text = "Daftar Riwayat Belajar:",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onBackground
                )

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .semantics {
                            contentDescription = "Daftar sesi belajar"
                        }
                ) {
                    items(logs) { log ->
                        Text(
                            text = "- Tanggal ${log.date}, durasi ${log.durationInMinutes} menit",
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
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, "Total durasi belajar saya adalah $total menit.")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, "Bagikan Riwayat Belajar"))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Tombol untuk membagikan riwayat belajar" },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Bagikan Riwayat Belajar", color = colorScheme.onPrimary)
            }

            OutlinedButton(
                onClick = { navController.navigate(Screen.Home.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "Tombol untuk kembali ke layar utama" }
            ) {
                Text("Kembali ke Halaman Utama", color = colorScheme.onSurface)
            }
        }
    }
}