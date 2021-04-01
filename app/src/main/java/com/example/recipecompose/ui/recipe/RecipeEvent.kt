package com.example.recipecompose.ui.recipe

sealed class RecipeEvent {
    data class GetRecipeEvent(val id: Int) : RecipeEvent()
}