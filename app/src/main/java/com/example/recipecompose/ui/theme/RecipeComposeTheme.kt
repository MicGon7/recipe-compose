package com.example.recipecompose.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.recipecompose.ui.components.CircularIndeterminateProgressBar
import com.example.recipecompose.ui.components.DefaultSnackbar

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
    displayProgressBar: Boolean,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) DarkThemeColors else LightThemeColors,
        typography = QuickSandTypography,
        shapes = AppShapes
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = if (!darkTheme) Grey1 else Black)
        ) {
            content()
            CircularIndeterminateProgressBar(isDisplayed = displayProgressBar, verticalBias = 0.4f)
            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
