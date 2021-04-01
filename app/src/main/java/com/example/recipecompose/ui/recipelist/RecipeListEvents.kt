package com.example.recipecompose.ui.recipelist.events

sealed class RecipeListEvent {
    object NewSearchEvent : RecipeListEvent()

    object NextPageEvent : RecipeListEvent()
}