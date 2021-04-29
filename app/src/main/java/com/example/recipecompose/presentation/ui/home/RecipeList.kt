package com.example.recipecompose.presentation.ui.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.presentation.components.cards.RecipeCard
import com.example.recipecompose.presentation.ui.home.HomeEvent
import com.example.recipecompose.util.RECIPE_PAGINATION_PAGE_SIZE

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onNextPage: (HomeEvent) -> Unit,
    loading: Boolean = false,
    navigateToRecipeDetail: (recipeId: Int) -> Unit

) {
    LazyColumn {
        itemsIndexed(recipes) { index, recipe ->
            onChangeRecipeScrollPosition(index)
            if ((index + 1) >= (page * RECIPE_PAGINATION_PAGE_SIZE) && !loading) {
                onNextPage(HomeEvent.NextPageEvent)
            }
            RecipeCard(
                recipe = recipe,
                onClick = {
                    navigateToRecipeDetail(recipe.id)
                })
        }
    }
}