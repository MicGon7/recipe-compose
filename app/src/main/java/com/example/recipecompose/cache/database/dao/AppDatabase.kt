package com.example.recipecompose.cache.database.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.recipecompose.cache.RecipeDao
import com.example.recipecompose.cache.converters.RecipeTypeConverters
import com.example.recipecompose.cache.model.RecipeEntity

const val DATABASE_NAME = "recipe-database"

/**
 * The Room database for this app
 */
@Database(entities = [RecipeEntity::class], version = 1)
@TypeConverters(RecipeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}