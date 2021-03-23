package com.example.recipecompose.ui

import androidx.annotation.StringRes
import com.example.recipecompose.R


sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home: Screen("home", R.string.home)
    object Other: Screen("other", R.string.other)
    object RecipeDetail: Screen("recipe", R.string.recipe_detail)
}
