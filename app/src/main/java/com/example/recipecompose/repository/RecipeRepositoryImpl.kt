package com.example.recipecompose.repository

import com.example.recipecompose.database.RecipeDao
import com.example.recipecompose.database.RecipeEntity
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.network.RecipeService
import com.example.recipecompose.network.model.RecipeDtoMapper
import javax.inject.Inject

class RecipeRepositoryImpl(
    private val recipeService: RecipeService,
    private val mapper: RecipeDtoMapper,
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        return mapper.toDomainList(
            recipeService.search(
                token = token,
                page = page,
                query = query
            ).recipes
        ).also {
            recipeDao.insertAll(it.map { recipe ->
                RecipeEntity.from(recipe)
            })
        }
    }

    override suspend fun get(token: String, id: Int): Recipe {
        return mapper.mapToDomainModel(recipeService.get(token = token, id))
    }

}