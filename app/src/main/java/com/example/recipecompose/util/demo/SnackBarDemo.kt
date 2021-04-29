package com.example.recipecompose.util.demo

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.recipecompose.util.TAG
import kotlinx.coroutines.launch


@Composable
fun SimpleSnackBar(
    show: Boolean,
    onHideSnackBar: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        if (show) {
            val snackBar = createRef()
            val bottomGuideLine = createGuidelineFromBottom(.10f)

            Snackbar(
                modifier = Modifier.constrainAs(snackBar) {
                    bottom.linkTo(bottomGuideLine)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                action = {
                    TextButton(onClick = {
                        onHideSnackBar()
                    }) {
                        Text(
                            "Hide",
                            style = MaterialTheme.typography.h5
                        )
                    }
                }
            ) {
                Text(text = "Hey! This is a SnackBar")
            }
        }
    }
}

@Composable
fun SimpleSnackBarDemo() {
    var showSnackbar by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = {
                showSnackbar = true
            }
        ) {
            Text("Show snackbar")
        }
        SimpleSnackBar(showSnackbar) {
            showSnackbar = false
        }
    }
}

@Composable
fun DecoupledSnackbar(
    snackbarHostState: SnackbarHostState
){
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val snackbar = createRef()
        val bottomGuideLine = createGuidelineFromBottom(.10f)
        SnackbarHost(
            modifier = Modifier.constrainAs(snackbar) {
                bottom.linkTo(bottomGuideLine)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            hostState = snackbarHostState,
            snackbar = {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                            }
                        ){
                            Text(
                                text = snackbarHostState.currentSnackbarData?.actionLabel?: "",
                                style = TextStyle(color = Color.White)
                            )
                        }
                    }
                ) {
                    Text(snackbarHostState.currentSnackbarData?.message?: "")
                }
            }
        )
    }
}

@Composable
fun DecoupledSnackBarDemo() {
    val snackbarHostState = remember{SnackbarHostState()}
    val scope = rememberCoroutineScope()

    Column {
        Button(
            onClick = {
                scope.launch {
                    val time = System.currentTimeMillis()
                    Log.d(TAG, "showing snackbar")
                    snackbarHostState.showSnackbar(
                        message = "Hey look a snackbar",
                        actionLabel = "Hide",
                        duration = SnackbarDuration.Short
                    )
                    Log.d(TAG, "done ${System.currentTimeMillis()-time}") // <-- Never called
                }
            }
        ) {
            Text("Show snackbar")
        }
        DecoupledSnackbar(snackbarHostState = snackbarHostState)
    }
}