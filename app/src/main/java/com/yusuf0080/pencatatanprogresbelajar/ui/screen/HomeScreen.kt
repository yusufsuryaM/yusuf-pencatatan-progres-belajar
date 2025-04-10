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

fun formatDurationFull(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return if (minutes >= 60) {
        val hours = minutes / 60
        val mins = minutes % 60
        if (mins > 0) "$hours jam $mins menit" else "$hours jam"
    } else {
        "$minutes menit $remainingSeconds detik"
    }
}

@Composable
fun HomeScreen(navController: NavController, addLog: (StudyLog) -> Unit) {
    var time by rememberSaveable { mutableIntStateOf(0) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val imageRes = if (isRunning) R.drawable.terbuka else R.drawable.tertutup
    val imageDesc = if (isRunning) "Buku terbuka - sedang belajar" else "Buku tertutup - sesi berhenti"

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            time++
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Pencatatan Progres Belajar")
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

            Text(
                text = "Durasi Belajar: ${formatDurationFull(time)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.semantics {
                    contentDescription = "Waktu belajar saat ini: ${formatDurationFull(time)}"
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { isRunning = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047))
                ) {
                    Text("Mulai Belajar", color = Color.White)
                }

                Button(
                    onClick = { isRunning = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFB8C00))
                ) {
                    Text("Jeda", color = Color.White)
                }

                Button(
                    onClick = {
                        isRunning = false
                        time = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("Atur Ulang", color = Color.White)
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
                        Toast.makeText(context, "Sesi belajar berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        time = 0
                    } else {
                        Toast.makeText(context, "Belum ada durasi belajar yang dicatat", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .semantics { contentDescription = "Tombol simpan sesi belajar" },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E88E5))
            ) {
                Text("Simpan Sesi Belajar", color = Color.White)
            }

            OutlinedButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, "Hari ini saya belajar selama ${formatDurationFull(time)}!")
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(intent, "Bagikan progres belajar"))
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .semantics { contentDescription = "Tombol bagikan progres belajar" }
            ) {
                Text("Bagikan Progres", color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(Screen.History.route) },
                    modifier = Modifier.semantics {
                        contentDescription = "Navigasi ke halaman riwayat belajar"
                    }
                ) {
                    Text("Lihat Riwayat", color = MaterialTheme.colorScheme.onSurface)
                }

                OutlinedButton(
                    onClick = { navController.navigate(Screen.Stats.route) },
                    modifier = Modifier.semantics {
                        contentDescription = "Navigasi ke halaman statistik belajar"
                    }
                ) {
                    Text("Lihat Statistik", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}