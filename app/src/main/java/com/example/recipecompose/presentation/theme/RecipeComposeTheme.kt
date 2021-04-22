package com.example.recipecompose.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.recipecompose.presentation.components.DisplayErrorDialog
import com.example.recipecompose.presentation.components.GenericDialog
import com.example.recipecompose.presentation.components.GenericDialogInfo
import java.util.*

private val LightThemeColors = lightColors(
    primary = Blue600,
    primaryVariant = Blue400,
    onPrimary = White,
    secondary = White,
    secondaryVariant = Teal300,
    onSecondary = Black,
    error = RedErrorDark,
    onError = RedErrorLight,
    background = Grey1,
    onBackground = Black,
    surface = White,
    onSurface = Black,
)

private val DarkThemeColors = darkColors(
    primary = Blue700,
    primaryVariant = White,
    onPrimary = White,
    secondary = Black1,
    onSecondary = White,
    error = RedErrorLight,
    background = Black,
    onBackground = White,
    surface = Black1,
    onSurface = White,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = QuickSandTypography,
        shapes = AppShapes
    ) {
        content()
    }
}
