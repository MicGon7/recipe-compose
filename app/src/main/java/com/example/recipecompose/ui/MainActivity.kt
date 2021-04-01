package com.example.recipecompose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavArgs
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.recipecompose.BaseApplication
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.ui.components.*
import com.example.recipecompose.ui.recipe.Other
import com.example.recipecompose.ui.recipe.RecipeDetail
import com.example.recipecompose.ui.recipe.RecipeViewModel
import com.example.recipecompose.ui.recipelist.events.RecipeListEvent.NewSearchEvent
import com.example.recipecompose.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Temp until DateStore impl
    @Inject
    lateinit var baseApplication: BaseApplication

    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(darkTheme = baseApplication.isDark) {
                RecipeActivityScreen(viewModel, baseApplication)
            }
        }
    }
}

@Composable
fun RecipeActivityScreen(viewModel: RecipeListViewModel, baseApplication: BaseApplication) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Home, Screen.Other)
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SearchAppBar(
                query = viewModel.query,
                onQueryChange = viewModel::onQueryChange,
                selectedCategory = viewModel.selectedCategory,
                onSelectCategoryChange = viewModel::onSelectedCategoryChange,
                onSearch = {
                    if (viewModel.selectedCategory?.value == "Milk") {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Invalid Category: MILK!",
                                actionLabel = "Hide"
                            )
                        }
                    } else {
                        viewModel.onTriggerEvent(NewSearchEvent)
                    }
                },
                scrollPosition = viewModel.categoryScrollPosition,
                onScrollPositionChange = viewModel::onScrollPositionChange,
                onToggleTheme = {
                    baseApplication.toggleLightTheme()
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController, screens)
        },
        scaffoldState = scaffoldState,
        snackbarHost = {
            scaffoldState.snackbarHostState
        },
    ) {
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                RecipeList(
                    navController, // Replace this with lambda when parcelables are supported
                    viewModel.recipes,
                    viewModel.loading,
                    scaffoldState.snackbarHostState,
                    viewModel.page,
                    viewModel::onChangeRecipeScrollPosition,
                    viewModel::onTriggerEvent
                )
            }
            composable(Screen.Other.route) { Other() }
            composable(
                route = "${Screen.RecipeDetail.route}/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.IntType})
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1

                val recipeViewModel = hiltNavGraphViewModel<RecipeViewModel>()
                RecipeDetail(recipeViewModel, recipeId)
            }
        }
    }
}