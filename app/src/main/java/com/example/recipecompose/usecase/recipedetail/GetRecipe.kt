package com.example.recipecompose.usecase.recipedetail

import android.util.Log
import com.example.recipecompose.data.cache.RecipeDao
import com.example.recipecompose.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.data.network.RecipeService
import com.example.recipecompose.data.network.model.RecipeDtoMapper
import com.example.recipecompose.domain.data.DataState
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
) {
    fun execute(
        recipeId: Int,
        token: String
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading())

            delay(1000)

            // Should never be null because a a list a recipes should already be added to database for query
            var recipe = getRecipeFromCache(recipeId = recipeId)

            if (recipe != null) {
                emit(DataState.success(recipe))
            } else {
                val networkRecipe = getRecipeFromNetwork(token, recipeId)

                recipeDao.insertRecipe(
                    entityMapper.mapFromDomainModel(networkRecipe)
                )

                recipe = getRecipeFromCache(recipeId)

                if (recipe != null) {
                    emit(DataState.success(recipe))
                } else {
                    throw Exception("Unable to get the recipe from the cache")
                }
            }


        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Recipe>(e.message ?: "unknown error"))

        }

    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }

    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return dtoMapper.mapToDomainModel(recipeService.get(token, recipeId))
    }
}