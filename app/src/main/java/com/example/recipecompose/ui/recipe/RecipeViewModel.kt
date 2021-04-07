package com.example.recipecompose.ui.recipe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.repository.RecipeRepository
import com.example.recipecompose.ui.recipe.RecipeDetailEvents.GetRecipeEvent
import com.example.recipecompose.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    @Named("auth_token")
    private val token: String
) : ViewModel() {
    var recipe: Recipe? by mutableStateOf(null)
        private set

    var loading by mutableStateOf(false)
        private set


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

    private suspend fun getRecipe(id: Int) {
        loading = true
        delay(1000)
        val recipe = recipeRepository.get(token = token, id = id)
        this.recipe = recipe

    }
}
