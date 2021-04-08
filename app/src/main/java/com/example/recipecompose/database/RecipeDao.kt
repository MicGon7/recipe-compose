package com.example.recipecompose.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.recipecompose.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    //TODO: Use query string to get specific recipes based on title and ingredients - How to check ingredients???
    @Query("SELECT * FROM recipes ORDER BY title")
    suspend fun getRecipes(): List<RecipeEntity>?

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipe(recipeId: String): RecipeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recipes: List<RecipeEntity>)
}