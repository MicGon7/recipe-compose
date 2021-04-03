package com.example.recipecompose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.recipecompose.BaseApplication
import com.example.recipecompose.ui.components.BottomNavBar
import com.example.recipecompose.ui.recipelist.RecipeList
import com.example.recipecompose.ui.components.SearchAppBar
import com.example.recipecompose.ui.recipe.Other
import com.example.recipecompose.ui.recipe.RecipeDetail
import com.example.recipecompose.ui.recipe.RecipeEvent
import com.example.recipecompose.ui.recipe.RecipeViewModel
import com.example.recipecompose.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Temp until DateStore impl
    @Inject
    lateinit var baseApplication: BaseApplication

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeApp(baseApplication)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun RecipeApp(baseApplication: BaseApplication) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Home, Screen.Other)
    val scaffoldState = rememberScaffoldState()
    val recipeListViewModel = hiltNavGraphViewModel<RecipeListViewModel>()

    AppTheme(
        displayProgressBar = recipeListViewModel.loading,
        scaffoldState = scaffoldState,
        darkTheme = baseApplication.isDark

    ) {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = !recipeListViewModel.showDetail,
                    enter = slideInVertically(initialOffsetY = { -40 })
                            + expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    SearchAppBar(
                        query = recipeListViewModel.query,
                        onQueryChange = recipeListViewModel::onQueryChange,
                        selectedCategory = recipeListViewModel.selectedCategory,
                        onSelectCategoryChange = recipeListViewModel::onSelectedCategoryChange,
                        onNewSearchEvent = recipeListViewModel::onTriggerEvent,
                        scrollPosition = recipeListViewModel.categoryScrollPosition,
                        onScrollPositionChange = recipeListViewModel::onScrollPositionChange,
                        onToggleTheme = {
                            baseApplication.toggleLightTheme()
                        },
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    !recipeListViewModel.showDetail,
                    enter = slideInVertically(initialOffsetY = { -40 })
                            + expandVertically(expandFrom = Alignment.Bottom) + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    BottomNavBar(
                        navController,
                        screens
                    )
                }
            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
        ) {
            NavHost(navController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    recipeListViewModel.onShowDetail(false)

                    RecipeList(
                        recipeListViewModel,
                        scaffoldState.snackbarHostState,
                    ) { recipeId ->
                        navController.navigate("${Screen.RecipeDetail.route}/$recipeId")
                    }
                }
                composable(Screen.Other.route) { Other() }
                composable(
                    route = "${Screen.RecipeDetail.route}/{recipeId}",
                    arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
                    val recipeViewModel = hiltNavGraphViewModel<RecipeViewModel>()

                    // Set the current recipe in the viewModel using it in navigation destination
                    recipeViewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))

                    recipeListViewModel.onShowDetail(true)

                    RecipeDetail(recipeViewModel)
                }
            }
        }
    }
}