package com.example.recipecompose.presentation.ui.recipedetails

sealed class RecipeDetailEvents {
    data class GetRecipeEvent(val id: Int) : RecipeDetailEvents()
}