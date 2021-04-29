package com.example.recipecompose.util.demo

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recipecompose.R
import com.example.recipecompose.presentation.components.buttons.AnimatedHeartButton


@Composable
fun AnimateAsStateDemo() {
    var blue by remember { mutableStateOf(true) }
    val color by animateColorAsState(if (blue) Blue else Color.Yellow)

    DemoColumn {
        Button(
            onClick = { blue = !blue }
        ) {
            Text(text = "CHANGE COLOR")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(color)
        )
    }
}

// Animate Color and Size based on state
private enum class BoxState {
    Small,
    Large
}

@Composable
fun UpdateTransitionDemo() {
    var boxState by remember { mutableStateOf(BoxState.Small) }
    val transition = updateTransition(targetState = boxState, label = "box state")
    val color by transition.animateColor() { state ->
        when (state) {
            BoxState.Small -> Blue
            BoxState.Large -> Yellow
        }
    }

    val size by transition.animateDp(
        transitionSpec = {
            // Customize transition properties
            if (targetState == BoxState.Large) {
                spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            } else {
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                )
            }
        }
    ) { state ->
        when (state) {
            BoxState.Small -> 64.dp
            BoxState.Large -> 128.dp
        }
    }

    DemoColumn {
        Button(
            onClick = {
                boxState = when (boxState) {
                    BoxState.Small -> BoxState.Large
                    BoxState.Large -> BoxState.Small
                }
            }
        ) {
            Text(text = "CHANGE COLOR & SIZE")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(size)
                .background(color)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimateVisibilityDemo() {
    var visible by remember { mutableStateOf(true) }

    DemoColumn {
        Button(
            onClick = {
                visible = !visible
            }
        ) {
            Text(text = if (visible) "HIDE" else "SHOW")
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(
                // Animate in from parent start
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
            exit = fadeOut(
                animationSpec = tween(
                    durationMillis = 300, easing = FastOutLinearInEasing
                )
            )

        ) {
            Box(
                modifier = Modifier
                    .size(128.dp)
                    .background(Blue)
            )

        }
    }
}

@Composable
fun AnimateContentSizeDemo() {
    var expanded by remember { mutableStateOf(false) }
    DemoColumn {
        Button(
            onClick = { expanded = !expanded }
        ) {
            Text(text = if (expanded) "SHRINK" else "EXPAND")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .background(LightGray)
                .animateContentSize()
        ) {
            Text(
                text = stringResource(R.string.animate_content_size),
                fontSize = 16.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(16.dp),
                maxLines = if (expanded) Int.MAX_VALUE else 2

            )
        }
    }
}

private enum class DemoScene {
    Text,
    Icon
}

@Composable
fun CrossfadeDemo() {
    var scene by remember { mutableStateOf(DemoScene.Text) }

    DemoColumn {
        Button(
            onClick = {
                scene = when (scene) {
                    DemoScene.Text -> DemoScene.Icon
                    DemoScene.Icon -> DemoScene.Text
                }
            }
        ) {
            Text(text = "TOGGLE")
        }
        Spacer(modifier = Modifier.height(4.dp))

        Crossfade(
            targetState = scene,
            animationSpec = tween(durationMillis = 2000)
        ) { scene ->
            when (scene) {
                DemoScene.Text -> Text(
                    text = "Phone",
                    fontSize = 32.sp
                )
                DemoScene.Icon -> Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun DemoColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()
    }

}

@Composable
fun GradientDemo() {
    val colors = listOf(
        Blue,
        Red,
        Blue
    )

    val brush = linearGradient(
        colors = colors,
        start = Offset(200f, 200f),
        end = Offset(400f, 400f)
    )

    Surface(
        shape = MaterialTheme.shapes.small
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = brush)
        )
    }
}

@Composable
fun AnimationDemo() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimateAsStateDemo()
        UpdateTransitionDemo()
        AnimateContentSizeDemo()
        AnimateVisibilityDemo()
        AnimatedHeartButton()
        CrossfadeDemo()
    }
}