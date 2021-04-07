package com.example.recipecompose.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
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
import com.example.recipecompose.ui.components.CircularIndeterminateProgressBar
import com.example.recipecompose.ui.components.DefaultSnackbar
import com.example.recipecompose.ui.components.RecipeCard

@Composable
fun Home(
    homeViewModel: HomeViewModel = viewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToRecipeList: (Int) -> Unit
) {
    val loading = homeViewModel.loading
    val recipes = homeViewModel.recipes
    val onChangeRecipeScrollPosition = homeViewModel::onChangeRecipeScrollPosition
    val page = homeViewModel.page
    val onNextPage = homeViewModel::onTriggerEvent

    // TODO: Move this to a LoadingBox composable
    val animateAlpha = animateFloatAsState(targetValue = if (loading) 0.8f else 1f)
    val animatedProgress = animateFloatAsState(
        targetValue = if (loading) 0.8f else 1f,
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
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading) {
                        onNextPage(HomeScreenEvents.NextPageEvent)
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
}
