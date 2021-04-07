package com.example.recipecompose

import androidx.compose.animation.*
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.*
import com.example.recipecompose.ui.components.BottomNavBar
import com.example.recipecompose.ui.components.SearchAppBar
import com.example.recipecompose.ui.home.HomeNavigationGraph
import com.example.recipecompose.ui.home.HomeViewModel
import com.example.recipecompose.ui.home.Screen
import com.example.recipecompose.ui.theme.AppTheme

@ExperimentalAnimationApi
@Composable
fun RecipeApp(baseApplication: BaseApplication) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Home, Screen.Other)
    val homeViewModel = hiltNavGraphViewModel<HomeViewModel>()
    val scaffoldState = rememberScaffoldState()

    AppTheme(darkTheme = baseApplication.isDark) {
        Scaffold(
            topBar = {
                AnimatedVisibility(
                    visible = homeViewModel.showNavigationBars,
                    enter = slideInVertically(initialOffsetY = { -40 })
                            + expandVertically(expandFrom = Alignment.Top)
                            + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically() + shrinkVertically() + fadeOut()
                ) {
                    SearchAppBar(
                        homeViewModel,
                        baseApplication,
                        scaffoldState
                    )

                }
            },
            bottomBar = {
                AnimatedVisibility(
                    homeViewModel.showNavigationBars,
                    enter = slideInVertically(initialOffsetY = { -40 })
                            + expandVertically(expandFrom = Alignment.Bottom)
                            + fadeIn(initialAlpha = 0.3f),
                    exit = slideOutVertically(targetOffsetY = { 40 })
                            + shrinkVertically(shrinkTowards = Alignment.Bottom)
                            + fadeOut()
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
            HomeNavigationGraph(
                navController = navController,
                homeViewModel = homeViewModel,
                scaffoldState = scaffoldState
            )
        }
    }
}
