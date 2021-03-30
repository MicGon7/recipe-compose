package com.example.recipecompose.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

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
    darkTheme: Boolean,
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
