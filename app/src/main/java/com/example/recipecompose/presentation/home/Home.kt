package com.example.recipecompose.presentation.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecompose.presentation.components.*
import com.example.recipecompose.util.RECIPE_PAGINATION_PAGE_SIZE

@Composable
fun Home(
    viewModel: HomeViewModel = viewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToRecipeList: (Int) -> Unit
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
            LazyColumn {
                itemsIndexed(recipes) { index, recipe ->
                    onChangeRecipeScrollPosition(index)
                    if ((index + 1) >= (page * RECIPE_PAGINATION_PAGE_SIZE) && !loading) {
                        onNextPage(HomeEvent.NextPageEvent)
                    }
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            navigateToRecipeList(recipe.id)
                        })
                }
            }
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
