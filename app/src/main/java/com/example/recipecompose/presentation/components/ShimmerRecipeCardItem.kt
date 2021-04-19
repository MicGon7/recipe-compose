package com.example.recipecompose.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerRecipeCardItem(
    colors: List<Color>,
    cardHeight: Dp,
    xShimmerAnim: Float = 0f,
    yShimmerAnim: Float = 0f,
    gradientWidth: Float = 0f,
    padding: Dp
) {

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(xShimmerAnim - gradientWidth, yShimmerAnim - gradientWidth),
        end = Offset(xShimmerAnim, yShimmerAnim)
    )

    Column(
        modifier = Modifier.padding(padding)
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            elevation = 16.dp
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(brush = brush)
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        Surface(
            shape = MaterialTheme.shapes.small,
            elevation = 16.dp
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight / 10)
                    .background(brush = brush)
            )
        }
    }
}
