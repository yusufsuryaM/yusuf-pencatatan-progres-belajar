package com.yusuf0080.pencatatanprogresbelajar.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object History : Screen("history")
    data object Stats : Screen("stats")
}