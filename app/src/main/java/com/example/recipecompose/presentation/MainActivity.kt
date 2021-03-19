package com.example.recipecompose.presentation

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.recipecompose.domain.model.Recipe

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: RecipeListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeActivityScreen(viewModel)
        }
    }
}

@Composable
fun RecipeActivityScreen(viewModel: RecipeListViewModel) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home,
        Screen.Other,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Recipes App") },
            )
        },
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
                items.forEach { screen ->
                    val currentIcon = when (screen) {
                        Screen.Home -> Icons.Filled.Home
                        Screen.Other -> Icons.Filled.Favorite
                        else -> {
                            Icons.Filled.Home
                        }
                    }
                    BottomNavigationItem(
                        // Requires contentDescription
                        icon = { Icon(currentIcon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo = navController.graph.startDestination
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) { HomeScreen(viewModel, navController) }
            composable(Screen.Other.route) { Other() }
            composable(
                "${Screen.RecipeDetail.route}/{recipeId}",
                arguments = listOf(navArgument("recipeId") {
                    // Not using optional args so this can remain empty
                })
            ) { backStackEntry ->
                Recipe(backStackEntry.arguments?.getString("recipeId"))
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: RecipeListViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "RecipeList",
            fontSize = 21.sp,
        )
        Spacer(modifier = Modifier.padding(4.dp))
        Button(
            onClick = {
                viewModel.newSearch()
            },
        ) {
            Text(text = "PERFORM SEARCH")
        }
        RecipeList(viewModel.recipes, navController)

    }
}

@Composable
fun RecipeList(recipes: List<Recipe>, navController: NavController) {
    LazyColumn {
        items(recipes) { recipe ->
            Text(
                text = recipe.title, fontSize = 21.sp,
                modifier = Modifier.clickable {
                    navController.navigate("${Screen.RecipeDetail.route}/${recipe.id}")
                })
        }
    }
}

@Composable
fun Recipe(recipeId: String?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "This is with id $recipeId",
            fontSize = 21.sp
        )
    }
}

@Composable
fun Other() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Other Screen", modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 21.sp
        )
    }
}
