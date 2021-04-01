package com.example.recipecompose.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.recipecompose.R


@Composable
fun AnimatedHeartButton() {
    var heartState by remember { mutableStateOf(HeartState.Idle) }
    val idleImageSize = 50.dp
    val activeImageSize = 55.dp

    val transition = updateTransition(targetState = heartState, label = "Heart")
    val imageSize by transition.animateDp(
        transitionSpec = {
            when {
                HeartState.Idle isTransitioningTo HeartState.Active ->
                    spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = 50f)
                else ->
                    tween(durationMillis = 500)
            }
        }
    ) { state ->
        when (state) {
            HeartState.Idle -> idleImageSize
            HeartState.Active -> activeImageSize
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Crossfade(
            targetState = heartState,
            animationSpec = tween(durationMillis = 1000),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) { state ->
            when (state) {
                HeartState.Idle -> Image(
                    painter = painterResource(id = R.drawable.heart_grey),
                    contentDescription = "Gray Heart",
                    modifier = Modifier
                        .size(imageSize)
                        .clickable {
                            heartState = HeartState.Active
                        }

                )
                HeartState.Active -> Image(
                    painter = painterResource(id = R.drawable.heart_red),
                    contentDescription = "Red Heart",
                    modifier = Modifier
                        .size(imageSize)
                        .clickable {
                            heartState = HeartState.Idle
                        }
                )
            }
        }
    }
}

private enum class HeartState { Idle, Active }