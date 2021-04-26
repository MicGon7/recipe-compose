package com.example.recipecompose.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun ConnectivityMonitor(
    isNetworkAvailable: Boolean,
) {
    AnimatedVisibility(
        visible = !isNetworkAvailable,
        enter = slideInVertically(initialOffsetY = { -40 })
                + expandVertically(expandFrom = Alignment.Top),
        exit = slideOutVertically() + shrinkVertically()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "No network connection",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp),
                style = MaterialTheme.typography.h6
            )
        }
    }
}