package com.example.recipecompose.presentation.ui.screens.recipedetails

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecompose.presentation.components.loading.CircularIndeterminateProgressBar

@Composable
fun RecipeDetailScreen(
    viewModel: RecipeDetailViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    if (viewModel.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 52.dp)
        ) {
            LoadingRecipeDetail(imageSize = 250.dp)
            CircularIndeterminateProgressBar(
                isDisplayed = viewModel.loading,
                verticalBias = 0.3f
            )
        }
    } else {
        viewModel.recipe?.let {
            RecipeItem(
                recipe = it,
                ingredientScrollState = scrollState
            )
        }
    }
}
