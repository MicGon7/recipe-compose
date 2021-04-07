package com.example.recipecompose.ui.recipe

sealed class RecipeDetailEvents {
    data class GetRecipeEvent(val id: Int) : RecipeDetailEvents()
}