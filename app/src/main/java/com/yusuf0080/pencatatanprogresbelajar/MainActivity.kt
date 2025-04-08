package com.yusuf0080.pencatatanprogresbelajar

import PencatatanProgresBelajarTheme
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateListOf
import androidx.navigation.compose.rememberNavController
import com.yusuf0080.pencatatanprogresbelajar.data.model.StudyLog
import com.yusuf0080.pencatatanprogresbelajar.navigation.SetupNavGraph
import com.yusuf0080.pencatatanprogresbelajar.ui.theme.PencatatanProgresBelajarTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val logs = mutableStateListOf<StudyLog>()
            val navController = rememberNavController()

            PencatatanProgresBelajarTheme {
                SetupNavGraph(
                    navController = navController,
                    logs = logs,
                    addLog = { logs.add(it) }
                )
            }
        }
    }
}