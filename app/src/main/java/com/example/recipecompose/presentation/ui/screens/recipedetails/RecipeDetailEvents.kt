package com.example.recipecompose.presentation.ui.screens.recipedetails

sealed class RecipeDetailEvents {
    data class GetRecipeEvent(val id: Int) : RecipeDetailEvents()
}