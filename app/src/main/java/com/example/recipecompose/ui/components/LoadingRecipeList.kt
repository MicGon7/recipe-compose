package com.example.recipecompose.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingRecipeList(imageSize: Dp) {
    val transition = rememberInfiniteTransition()
    val initialValue = 200f
    val targetValue = 2000f
    val durationMillis = 1300

    val xTransitionAnim by transition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = durationMillis, easing = LinearEasing),
        )
    )
    val yTransitionAnim by transition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = durationMillis, easing = LinearEasing),
        )
    )

    val colors = listOf(
        Color.LightGray.copy(alpha = .9f),
        Color.LightGray.copy(alpha = .3f),
        Color.LightGray.copy(alpha = .9f),
    )

    LazyColumn {
        items(5) {
            ShimmerRecipeCardItem(
                colors = colors,
                cardHeight = imageSize,
                xShimmerAnim = xTransitionAnim,
                yShimmerAnim = yTransitionAnim,
                gradientWidth = 200f,
                padding = 16.dp
            )
        }
    }
}