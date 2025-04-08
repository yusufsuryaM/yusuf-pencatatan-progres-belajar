package com.yusuf0080.pencatatanprogresbelajar.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class StatsViewModel : ViewModel() {
    var weeklyTarget by mutableIntStateOf(360)
    var dailyTarget by mutableIntStateOf(60)
}