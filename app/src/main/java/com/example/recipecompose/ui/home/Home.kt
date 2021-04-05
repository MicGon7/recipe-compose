package com.example.recipecompose.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecompose.ui.PAGE_SIZE
import com.example.recipecompose.ui.HomeViewModel
import com.example.recipecompose.ui.components.CircularIndeterminateProgressBar
import com.example.recipecompose.ui.components.DefaultSnackbar
import com.example.recipecompose.ui.components.RecipeCard

@Composable
fun Home(
    recipeListViewModel: HomeViewModel = viewModel(),
    snackbarHostState: SnackbarHostState,
    navigateToRecipeList: (Int) -> Unit
) {
    val loading = recipeListViewModel.loading
    val recipes = recipeListViewModel.recipes
    val onChangeRecipeScrollPosition = recipeListViewModel::onChangeRecipeScrollPosition
    val page = recipeListViewModel.page
    val onNextPage = recipeListViewModel::onTriggerEvent


    // Box allows for overlay of composables--last composables shown on top
    Box(
        modifier = Modifier
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
                        onNextPage(RecipeListEvent.NextPageEvent)
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
