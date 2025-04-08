package com.yusuf0080.pencatatanprogresbelajar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yusuf0080.pencatatanprogresbelajar.ui.screen.*
import com.yusuf0080.pencatatanprogresbelajar.data.model.StudyLog

@Composable
fun SetupNavGraph(navController: NavHostController, logs: List<StudyLog>, addLog: (StudyLog) -> Unit) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(navController, addLog)
        }
        composable(Screen.History.route) {
            HistoryScreen(navController, logs)
        }
        composable(Screen.Stats.route) {
            StatsScreen(navController, logs)
        }
    }
}