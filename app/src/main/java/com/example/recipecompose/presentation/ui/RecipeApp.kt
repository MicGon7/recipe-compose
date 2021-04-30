package com.example.recipecompose.presentation.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.rememberNavController
import com.example.recipecompose.datastore.SettingsDataStore
import com.example.recipecompose.presentation.ui.navigation.BottomNavBar
import com.example.recipecompose.presentation.ui.navigation.SearchAppBar
import com.example.recipecompose.presentation.components.prompts.ConnectivityMonitor
import com.example.recipecompose.presentation.theme.AppTheme
import com.example.recipecompose.presentation.ui.navigation.HomeNavigationGraph
import com.example.recipecompose.presentation.ui.screens.home.HomeViewModel
import com.example.recipecompose.presentation.ui.screens.home.util.Screens

@ExperimentalAnimationApi
@Composable
fun RecipeApp(isNetworkAvailable: Boolean, settingsDataStore: SettingsDataStore) {
    val navController = rememberNavController()
    val screens = listOf(Screens.Home, Screens.Other)
    val homeViewModel = hiltNavGraphViewModel<HomeViewModel>()
    val scaffoldState = rememberScaffoldState()

    // TODO: Add global snackbar and loading indicator here
    AppTheme(darkTheme = settingsDataStore.isDark) {
        Column {
            ConnectivityMonitor(isNetworkAvailable = isNetworkAvailable)
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
                            settingsDataStore,
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
}