package com.example.recipecompose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.repository.RecipeRepository
import com.example.recipecompose.ui.components.FoodCategory
import com.example.recipecompose.ui.components.getFoodCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

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

    init {
        newSearch()
    }

    fun newSearch() {
        viewModelScope.launch {
            loading = true
            resetSearchState()
            delay(2000)
            val result = repository.search(
                token = token,
                page = 1,
                query = query
            )
            recipes = result

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

    private fun clearSelectedCategory() {
        selectedCategory = null
    }
    private fun resetSearchState() {
        recipes = listOf()
        if(selectedCategory?.value != query) {
            clearSelectedCategory()
        }
    }
}