package com.example.recipecompose.data.cache.converters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

open class RecipeTypeConverters {
    private val moshi = Moshi.Builder().build()
    private val ingredientsType = Types.newParameterizedType(List::class.java, String::class.java)
    private val ingredientsAdapter = moshi.adapter<List<String>>(ingredientsType)

    @TypeConverter
    fun stringToIngredients(string: String): List<String> {
        return ingredientsAdapter.fromJson(string).orEmpty()
    }

    @TypeConverter
    fun ingredientsToString(ingredients: List<String>): String {
        return ingredientsAdapter.toJson(ingredients)
    }
}