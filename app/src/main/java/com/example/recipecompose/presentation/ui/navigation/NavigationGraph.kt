package com.example.recipecompose.presentation.ui.navigation


import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import com.example.recipecompose.presentation.ui.screens.home.HomeScreen
import com.example.recipecompose.presentation.ui.screens.home.HomeViewModel
import com.example.recipecompose.presentation.ui.screens.home.util.HomeEvent
import com.example.recipecompose.presentation.ui.screens.home.util.Screens
import com.example.recipecompose.presentation.ui.screens.other.Other
import com.example.recipecompose.presentation.ui.screens.recipedetails.RecipeDetailScreen
import com.example.recipecompose.presentation.ui.screens.recipedetails.RecipeDetailEvents
import com.example.recipecompose.presentation.ui.screens.recipedetails.RecipeDetailViewModel


@Composable
fun HomeNavigationGraph(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(),
    scaffoldState: ScaffoldState,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            viewModel.onShowDetail(true)
            HomeScreen(
                loading = viewModel.loading,
                recipes = viewModel.recipes,
                onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                page = viewModel.page,
                onNextPage = viewModel::onTriggerEvent,
                dialogQueue = viewModel.dialogQueue.queue.value,
                scaffoldState.snackbarHostState,
            ) { recipeId ->
                navController.navigate("${Screens.RecipeDetail.route}/$recipeId")
            }
        }
        composable(Screens.Other.route) { Other() }
        composable(
            route = "${Screens.RecipeDetail.route}/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: -1
            val recipeViewModel = hiltNavGraphViewModel<RecipeDetailViewModel>()

            viewModel.onShowDetail(false)

            // Set the current recipe in the viewModel using it in navigation destination
            recipeViewModel.onTriggerEvent(RecipeDetailEvents.GetRecipeEvent(recipeId))

            RecipeDetailScreen(recipeViewModel)
        }
    }
}

