package com.example.recipecompose.presentation.ui.home


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
import com.example.recipecompose.presentation.ui.other.Other
import com.example.recipecompose.presentation.ui.recipedetails.RecipeDetail
import com.example.recipecompose.presentation.ui.recipedetails.RecipeDetailEvents
import com.example.recipecompose.presentation.ui.recipedetails.RecipeDetailViewModel


@Composable
fun HomeNavigationGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(),
    scaffoldState: ScaffoldState,
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route) {
            homeViewModel.onShowDetail(true)

            HomeScreen(
                homeViewModel,
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

            homeViewModel.onShowDetail(false)

            // Set the current recipe in the viewModel using it in navigation destination
            recipeViewModel.onTriggerEvent(RecipeDetailEvents.GetRecipeEvent(recipeId))

            RecipeDetail(recipeViewModel)
        }
    }
}

