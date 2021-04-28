package com.example.recipecompose.cache

import com.example.recipecompose.network.data.cache.model.RecipeEntity

class AppDatabaseFake {
    val recipes = mutableListOf<RecipeEntity>()
}