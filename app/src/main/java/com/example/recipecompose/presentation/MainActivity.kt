package com.example.recipecompose.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.recipecompose.components.RecipeCard
import com.example.recipecompose.domain.model.Recipe
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.accompanist.coil.CoilImage

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
    val bottomBarItems = listOf(Screen.Home, Screen.Other)
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
                bottomBarItems.forEach { screen ->
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
            composable(Screen.RecipeDetail.route) {
                // This is a workaround for a crash when using supported parcelable NavArg Type
                val recipeModel =
                    navController.previousBackStackEntry?.arguments?.getParcelable<Recipe>(Screen.RecipeDetail.route)

                RecipeDetail(recipe = recipeModel)
            }
        }
    }
}

@Composable
fun HomeScreen(viewModel: RecipeListViewModel, navController: NavController) {
    RecipeList(viewModel.recipes, navController)
}

@Composable
fun RecipeList(recipes: List<Recipe>, navController: NavController) {
    LazyColumn {
        items(recipes) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = {
                    // Navigation must remain in callback to avoid being called during recomp
                    navController.currentBackStackEntry?.arguments?.putParcelable(
                        Screen.RecipeDetail.route,
                        recipe
                    )
                    navController.navigate(Screen.RecipeDetail.route)
                })
        }
    }
}

@Composable
fun RecipeDetail(recipe: Recipe?) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "This is ${recipe?.title} with id ${recipe?.id}",
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
