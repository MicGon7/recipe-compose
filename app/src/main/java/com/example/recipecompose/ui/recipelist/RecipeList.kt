package com.example.recipecompose.ui.components

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
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.ui.PAGE_SIZE
import com.example.recipecompose.ui.Screen
import com.example.recipecompose.ui.recipelist.events.RecipeListEvent

@Composable
fun RecipeList(
    navController: NavController,
    recipes: List<Recipe>,
    loading: Boolean,
    snackbarHostState: SnackbarHostState,
    page: Int,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    onNextPage: (RecipeListEvent) -> Unit
) {
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
                            navController.navigate("${Screen.RecipeDetail.route}/${recipe.id}")
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
