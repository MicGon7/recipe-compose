package com.example.recipecompose.ui


import androidx.annotation.StringRes
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
import com.example.recipecompose.R
import com.example.recipecompose.ui.home.Home
import com.example.recipecompose.ui.other.Other
import com.example.recipecompose.ui.recipe.RecipeDetail
import com.example.recipecompose.ui.recipe.RecipeEvent
import com.example.recipecompose.ui.recipe.RecipeViewModel


@Composable
fun HomeNavigationGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    scaffoldState: ScaffoldState
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            homeViewModel.onShowDetail(true)

            Home(
                homeViewModel,
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

            homeViewModel.onShowDetail(false)

            // Set the current recipe in the viewModel using it in navigation destination
            recipeViewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))

            RecipeDetail(recipeViewModel)
        }
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Other : Screen("other", R.string.other)
    object RecipeDetail : Screen("recipe", R.string.recipe_detail)
}
