package com.example.recipecompose.presentation.ui.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.presentation.util.DialogQueue
import com.example.recipecompose.presentation.ui.home.HomeEvent.NewSearchEvent
import com.example.recipecompose.presentation.ui.home.HomeEvent.NextPageEvent
import com.example.recipecompose.presentation.util.CustomConnectivityManager
import com.example.recipecompose.interactors.home.RestoreRecipes
import com.example.recipecompose.interactors.home.SearchRecipes
import com.example.recipecompose.util.RECIPE_PAGINATION_PAGE_SIZE
import com.example.recipecompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchRecipes: SearchRecipes,
    private val restoreRecipes: RestoreRecipes,
    private val connectivityManager: CustomConnectivityManager,
    @Named("auth_token")
    private val token: String,
) : ViewModel() {

    var recipes: List<Recipe> by mutableStateOf(listOf())
        private set

    var query: String by mutableStateOf("")
        private set

    var selectedCategory: FoodCategory? by mutableStateOf(null)
        private set

    var categoryScrollPosition = 0
        private set

    var page by mutableStateOf(1)

    var loading by mutableStateOf(false)
        private set

    var showNavigationBars by mutableStateOf(true)
        private set

    private var recipeListScrollPosition = 0

    val dialogQueue = DialogQueue()

    init {
        onTriggerEvent(NewSearchEvent)
    }

    fun onTriggerEvent(event: HomeEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> {
                        newSearch()
                    }
                    is NextPageEvent -> {
                        nextPage()
                    }
                    is HomeEvent.RestoreStateEvent -> {
                        restoreState()
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    // This is a rare case but kept it here as an example
    private fun restoreState() {
        restoreRecipes.execute(
            page = page,
            query = query
        ).onEach { dataState ->
            loading = dataState.loading

            dataState.data?.let { list ->
                recipes = list
            }

            dataState.error?.let { error ->
                Log.e(TAG, "restoreState: $error")
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)

    }

    private fun newSearch() {
        Log.d(TAG, "newSearch: query: $query, page: $page")

        resetSearchState()
        searchRecipes.execute(
            token = token,
            page = page,
            query = query,
            isNetworkAvailable = connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading = dataState.loading

            dataState.data?.let { list ->
                recipes = list
            }

            dataState.error?.let { error ->
                Log.e(TAG, "newSearch: $error")
                dialogQueue.appendErrorMessage("Error", error)

            }
        }.launchIn(viewModelScope)
    }

    // TODO: Experiment with paging library instead
    private fun nextPage() {
        // Prevent duplicate events due to recompose happening to quickly
        if ((recipeListScrollPosition + 1) >= (page * RECIPE_PAGINATION_PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "next page triggered: $page")

            if (page > 1) {
                searchRecipes.execute(
                    token = token,
                    page = page,
                    query = query,
                    connectivityManager.isNetworkAvailable.value
                ).onEach { dataState ->
                    loading = dataState.loading

                    dataState.data?.let { list ->
                        appendRecipes(list)
                    }

                    dataState.error?.let { error ->
                        Log.e(TAG, "nextPage: $error")
                        dialogQueue.appendErrorMessage("Error", error)
                    }
                }.launchIn(viewModelScope)
            }
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

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    fun onShowDetail(showDetail: Boolean) {
        this.showNavigationBars = showDetail
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val currentRecipes = ArrayList(recipes)
        currentRecipes.addAll(recipes)

        this.recipes = currentRecipes

    }

    private fun incrementPage() {
        page += 1
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
}