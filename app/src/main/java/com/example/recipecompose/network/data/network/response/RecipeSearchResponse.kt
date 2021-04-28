package com.example.recipecompose.network.data.network.response

import com.example.recipecompose.network.data.network.model.RecipeDto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeSearchResponse(

    @Json(name = "count")
        var count: Int,

    @Json(name ="results")
        var recipes: List<RecipeDto>,
)