package com.example.recipecompose.network.data.cache.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.recipecompose.network.data.cache.RecipeDao
import com.example.recipecompose.network.data.cache.model.RecipeEntity

const val DATABASE_NAME = "recipe-database"

/**
 * The Room database for this app
 */
@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}