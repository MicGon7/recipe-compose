package com.example.recipecompose

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    // should be saved in data store
    var isDark: Boolean by mutableStateOf(false)

    fun toggleLightTheme() {
        isDark = !isDark
    }

}