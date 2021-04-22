package com.example.recipecompose.usecase.home

import com.example.recipecompose.data.cache.RecipeDao
import com.example.recipecompose.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.domain.data.DataState
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreRecipes(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper
) {
    fun execute(
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {

            emit(DataState.loading())
            delay(1000)

            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)
            emit(DataState.success(list))

        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown Error"))
        }
    }
}
