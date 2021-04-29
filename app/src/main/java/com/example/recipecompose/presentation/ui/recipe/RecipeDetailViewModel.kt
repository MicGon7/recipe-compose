package com.example.recipecompose.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.presentation.util.DialogQueue
import com.example.recipecompose.presentation.ui.recipe.RecipeDetailEvents.GetRecipeEvent
import com.example.recipecompose.presentation.util.CustomConnectivityManager
import com.example.recipecompose.interactors.recipe.GetRecipe
import com.example.recipecompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipe: GetRecipe,
    @Named("auth_token")
    private val token: String,
    private val connectivityManager: CustomConnectivityManager
) : ViewModel() {
    var recipe: Recipe? by mutableStateOf(null)
        private set

    var loading by mutableStateOf(false)
        private set

    val dialogQueue = DialogQueue()

    fun onTriggerEvent(event: RecipeDetailEvents) {
        viewModelScope.launch {
            loading = true
            try {
                when (event) {
                    is GetRecipeEvent -> {
                        if (recipe == null) {
                            getRecipe(event.id)
                        }
                        loading = false
                    }
                }


            } catch (e: Exception) {
                Log.d(TAG, "onTriggerEvent: Exception $e, ${e.cause}")
            }
        }
    }

    private fun getRecipe(id: Int) {
        getRecipe.execute(id).onEach { dataState ->
            loading = dataState.loading

            dataState.data?.let { data ->
                recipe = data
            }

            // Should never happen
            dataState.error?.let { error ->
                Log.e(TAG, "getRecipe $error")
                dialogQueue.appendErrorMessage("Error", error)
            }

        }.launchIn(viewModelScope)
    }
}
