package com.example.recipecompose.database

import androidx.room.Database
import androidx.room.RoomDatabase

const val DATABASE_NAME = "recipe-database"

/**
 * The Room database for this app
 */
@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}