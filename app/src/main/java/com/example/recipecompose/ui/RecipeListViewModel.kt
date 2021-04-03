package com.example.recipecompose.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.repository.RecipeRepository
import com.example.recipecompose.ui.components.FoodCategory
import com.example.recipecompose.ui.components.getFoodCategory
import com.example.recipecompose.ui.recipelist.RecipeListEvent
import com.example.recipecompose.ui.recipelist.RecipeListEvent.*
import com.example.recipecompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repository: RecipeRepository,
    @Named("auth_token")
    private val token: String,
) : ViewModel() {

    var recipes: List<Recipe> by mutableStateOf(listOf())
        private set

    var query: String by mutableStateOf("chicken")
        private set

    var selectedCategory: FoodCategory? by mutableStateOf(null)
        private set

    var categoryScrollPosition = 0
        private set

    var loading by mutableStateOf(false)
        private set

    var showDetail by mutableStateOf(false)
        private set

    var page by mutableStateOf(1)

    private var recipeListScrollPosition = 0

    init {
        onTriggerEvent(NewSearchEvent)
    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> {
                        newSearch()
                    }
                    is NextPageEvent -> {
                        nextPage()
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    // Usecase #1
    private suspend fun newSearch() {
        loading = true
        resetSearchState()
        delay(3000)
        val result = repository.search(
            token = token,
            page = 1,
            query = query
        )
        recipes = result

        loading = false
    }

    // Usecase #2
    private suspend fun nextPage() {
        // Prevent duplicate events due to recompose happening to quickly
        if ((recipeListScrollPosition + 1) >= (page * PAGE_SIZE)) {
            loading = true
            incrementPage()
            Log.d(TAG, "next page triggered: $page")

            // Just to show pagination, api is fast
            delay(1000)

            // Don't call when app first launches
            if (page > 1) {
                val result = repository.search(
                    token = token,
                    page = page,
                    query = query
                )
                Log.d(TAG, "next page: $result")
                appendRecipes(result)
            }
            loading = false
        }
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }

    fun onSelectedCategoryChange(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory = newCategory
        onQueryChange(category)
    }

    fun onScrollPositionChange(position: Int) {
        categoryScrollPosition = position
    }

    /**
     * Append new recipes to current list of recipes
     */

    fun appendRecipes(recipes: List<Recipe>) {
        val currentRecipes = ArrayList(recipes)
        currentRecipes.addAll(recipes)

        this.recipes = currentRecipes

    }

    private fun incrementPage() {
        page += 1
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    private fun clearSelectedCategory() {
        selectedCategory = null
    }

    private fun resetSearchState() {
        recipes = listOf()
        page = 1
        onChangeRecipeScrollPosition(0)
        if (selectedCategory?.value != query) {
            clearSelectedCategory()
        }
    }

    fun onShowDetail(showDetail: Boolean) {
        this.showDetail = showDetail
    }
}
