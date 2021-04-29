package com.example.recipecompose.presentation.ui.recipe

sealed class RecipeDetailEvents {
    data class GetRecipeEvent(val id: Int) : RecipeDetailEvents()
}