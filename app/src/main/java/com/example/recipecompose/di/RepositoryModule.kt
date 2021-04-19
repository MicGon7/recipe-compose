package com.example.recipecompose.di

import com.example.recipecompose.data.cache.RecipeDao
import com.example.recipecompose.data.network.RecipeService
import com.example.recipecompose.data.network.model.RecipeDtoMapper
import com.example.recipecompose.repository.RecipeRepository
import com.example.recipecompose.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        recipeService: RecipeService,
        recipeMapper: RecipeDtoMapper,
        recipeDao: RecipeDao
    ): RecipeRepository {
        return RecipeRepositoryImpl(
            recipeService = recipeService,
            mapper = recipeMapper,
            recipeDao = recipeDao
        )
    }
}

