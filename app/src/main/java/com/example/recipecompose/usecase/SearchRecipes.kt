package com.example.recipecompose.usecase

import com.example.recipecompose.data.cache.RecipeDao
import com.example.recipecompose.data.cache.model.RecipeEntityMapper
import com.example.recipecompose.domain.data.DataState
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.data.network.RecipeService
import com.example.recipecompose.data.network.model.RecipeDtoMapper
import com.example.recipecompose.util.RECIPE_PAGINATION_PAGE_SIZE
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn

class SearchRecipes(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
) {
    fun execute(
        token: String,
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())

            // just to show pagination, api is fast
            delay(1000)

            // force error for testing
            if (query == "error") {
                throw Exception("Search Failed!")
            }

            // TODO("Check if there is an internet connection")
            // Convert: NetworkRecipeEntity -> Recipe > RecipeCacheEntity
            val recipes = getRecipesFromNetwork(
                token = token,
                page = page,
                query = query
            )

            // insert into cache
            recipeDao.insertRecipes(entityMapper.toEntityList(recipes))

            // query the cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            // emit List<Recipe> from cache
            val list = entityMapper.fromEntityList(cacheResult)

            emit(DataState.success(list))

        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown Error"))
        }
    }

    // WARNING: This will throw exception if there is no network connection
    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String
    ): List<Recipe> {
        return dtoMapper.toDomainList(
            recipeService.search(
                token = token,
                page = page,
                query = query
            ).recipes
        )
    }
}