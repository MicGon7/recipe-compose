package com.example.recipecompose.presentation.components.prompts

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.NetworkLocked
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recipecompose.R

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.error),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Error,
                contentDescription = null,
                modifier = Modifier.padding(start = 16.dp),
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                stringResource(R.string.no_network_connection),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .wrapContentSize(),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun PreviewConnectivityMonitor() {
    ConnectivityMonitor(isNetworkAvailable = false)
}