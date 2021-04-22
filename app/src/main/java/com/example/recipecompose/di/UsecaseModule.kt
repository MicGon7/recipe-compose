package com.example.recipecompose.di

import com.example.recipecompose.data.cache.RecipeDao
import com.example.recipecompose.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.data.network.RecipeService
import com.example.recipecompose.data.network.model.RecipeDtoMapper
import com.example.recipecompose.usecase.recipedetail.GetRecipe
import com.example.recipecompose.usecase.home.RestoreRecipes
import com.example.recipecompose.usecase.home.SearchRecipes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UsecaseModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipes(
        recipeService: RecipeService,
        recipeDao: RecipeDao,
        recipeEntity: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper
    ): SearchRecipes {
        return SearchRecipes(
            recipeService = recipeService,
            recipeDao = recipeDao,
            entityMapper = recipeEntity,
            dtoMapper = recipeDtoMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipe(
        recipeService: RecipeService,
        recipeDao: RecipeDao,
        recipeEntity: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper
    ): GetRecipe {
        return GetRecipe(
            recipeService = recipeService,
            recipeDao = recipeDao,
            entityMapper = recipeEntity,
            dtoMapper = recipeDtoMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDao: RecipeDao,
        recipeEntity: RecipeEntityMapper,
    ): RestoreRecipes {
        return RestoreRecipes(
            recipeDao = recipeDao,
            entityMapper = recipeEntity,
        )
    }
}