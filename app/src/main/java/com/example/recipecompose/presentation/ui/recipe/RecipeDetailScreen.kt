package com.example.recipecompose.presentation.ui.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecompose.presentation.components.loading.CircularIndeterminateProgressBar

@Composable
fun RecipeDetail(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            viewModel.recipe?.let {
                RecipeItem(recipe = it)
            }
        }
    }
}
