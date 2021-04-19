package com.example.recipecompose.presentation.recipe

sealed class RecipeDetailEvents {
    data class GetRecipeEvent(val id: Int) : RecipeDetailEvents()
}