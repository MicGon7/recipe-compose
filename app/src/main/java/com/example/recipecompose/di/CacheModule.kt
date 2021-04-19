package com.example.recipecompose.di

import android.content.Context
import androidx.room.Room
import com.example.recipecompose.data.cache.RecipeDao
import com.example.recipecompose.data.cache.database.dao.AppDatabase
import com.example.recipecompose.data.cache.database.dao.DATABASE_NAME
import com.example.recipecompose.data.cache.model.RecipeEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CacheModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideRecipeDao(appDatabase: AppDatabase): RecipeDao {
        return appDatabase.recipeDao()
    }

    @Singleton
    @Provides
    fun provideRecipeEntityMapper(): RecipeEntityMapper {
        return RecipeEntityMapper()
    }

}