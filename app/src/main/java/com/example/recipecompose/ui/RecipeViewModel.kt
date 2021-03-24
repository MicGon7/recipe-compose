package com.example.recipecompose.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.repository.RecipeRepository
import com.example.recipecompose.ui.recipelist.FoodCategory
import com.example.recipecompose.ui.recipelist.getFoodCategory
import dagger.hilt.android.lifecycle.HiltViewModel
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


    init {
        newSearch()
    }

    fun newSearch() {
        viewModelScope.launch {
            val result = repository.search(
                token = token,
                page = 1,
                query = query
            )
            recipes = result
        }
    }

    fun onQueryChange(newQuery: String) {
        query = newQuery
    }

    fun onSelectedCategory(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory = newCategory
        onQueryChange(category)
    }
}