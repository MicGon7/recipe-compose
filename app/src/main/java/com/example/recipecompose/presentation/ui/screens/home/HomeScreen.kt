package com.example.recipecompose.presentation.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecompose.presentation.components.loading.CircularIndeterminateProgressBar
import com.example.recipecompose.presentation.components.loading.LoadingRecipeList
import com.example.recipecompose.presentation.components.prompts.DefaultSnackbar
import com.example.recipecompose.presentation.components.prompts.DisplayErrorDialog
import com.example.recipecompose.presentation.ui.screens.home.components.RecipeList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToRecipeDetail: (Int) -> Unit
) {
    val loading = viewModel.loading
    val recipes = viewModel.recipes
    val onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition
    val page = viewModel.page
    val onNextPage = viewModel::onTriggerEvent

    val dialogQueue = viewModel.dialogQueue.queue.value

    // TODO: Move this to a LoadingBox composable
    val animateAlpha = animateFloatAsState(targetValue = if (loading) 0.8f else 1f)
    val animatedProgress = animateFloatAsState(
        targetValue = if (loading) 0.95f else 1f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )

    val modifier = Modifier.graphicsLayer(
        alpha = animateAlpha.value,
        scaleY = animatedProgress.value,
        scaleX = animatedProgress.value
    )

    // Box allows for overlay of composables--last composables shown on top
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 52.dp) // temp: place above bottom bar
    ) {
        if (loading && recipes.isEmpty()) {
            LoadingRecipeList(imageSize = 250.dp)
        } else {
            RecipeList(
                recipes = recipes,
                onChangeRecipeScrollPosition = onChangeRecipeScrollPosition,
                page = page,
                onNextPage = onNextPage,
                loading = loading,
                navigateToRecipeDetail = navigateToRecipeDetail
            )
        }
        CircularIndeterminateProgressBar(isDisplayed = loading, verticalBias = 0.4f)
        DefaultSnackbar(
            snackbarHostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
            onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() }
        )
    }
    DisplayErrorDialog(dialogQueue = dialogQueue)
}

@Preview
@Composable
fun HomeScreenPreview() {

}