package com.example.recipecompose.interactors.recipedetails

import com.example.recipecompose.cache.RecipeDao
import com.example.recipecompose.cache.model.RecipeEntityMapper
import com.example.recipecompose.domain.data.DataState
import com.example.recipecompose.domain.model.Recipe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
) {
    fun execute(
        recipeId: Int
    ): Flow<DataState<Recipe>> = flow {
        emit(DataState.loading())

        delay(1000)

        val recipe = getRecipeFromCache(recipeId = recipeId)
        // Recipe should never be null since it is pulled from a pre-populated database (home screen)
        recipe?.let {
            emit(DataState.success(recipe))
        }
    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }
    }
}