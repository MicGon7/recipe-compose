package com.example.recipecompose.ui.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.animation.core.RepeatMode.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.recipecompose.util.TAG

@Composable
fun PulsingDemo() {
    val color = MaterialTheme.colors.primary
    val infiniteTransition = rememberInfiniteTransition()

    val pulseMagnitude by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(300, easing = FastOutLinearInEasing),
            repeatMode = Reverse
        )
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = Restart
        )
    )
    val transition = updateTransition(
        targetState = "Start",
        label = "Heart"
    )
    Column(
        modifier = Modifier.animateContentSize(
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(pulseMagnitude.dp)
                    .rotate(rotation),
                imageVector = Icons.Default.Favorite,
                contentDescription = "Fav Icon"
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            drawCircle(
                radius = pulseMagnitude,
                brush = SolidColor(color)
            )
        }
    }

    Log.d(TAG, "PulsingDemo: $pulseMagnitude")
}