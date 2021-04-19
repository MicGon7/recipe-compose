package com.example.recipecompose.presentation.home

import androidx.annotation.StringRes
import com.example.recipecompose.R

sealed class Screens(val route: String, @StringRes val resourceId: Int) {
    object Home : Screens("home", R.string.home)
    object Other : Screens("other", R.string.other)
    object RecipeDetail : Screens("recipe", R.string.recipe_detail)
}
