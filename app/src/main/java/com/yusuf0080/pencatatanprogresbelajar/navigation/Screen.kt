package com.yusuf0080.pencatatanprogresbelajar.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object History : Screen("history")
    object Stats : Screen("stats")
}