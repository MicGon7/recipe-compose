package com.example.recipecompose.di

import com.example.recipecompose.network.data.cache.RecipeDao
import com.example.recipecompose.network.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.network.data.network.RecipeService
import com.example.recipecompose.network.data.network.model.RecipeDtoMapper
import com.example.recipecompose.interactors.recipe.GetRecipe
import com.example.recipecompose.interactors.home.RestoreRecipes
import com.example.recipecompose.interactors.home.SearchRecipes
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
        recipeDao: RecipeDao,
        recipeEntity: RecipeEntityMapper,
    ): GetRecipe {
        return GetRecipe(
            recipeDao = recipeDao,
            entityMapper = recipeEntity,
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