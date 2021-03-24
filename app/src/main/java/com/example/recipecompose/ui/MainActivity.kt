package com.example.recipecompose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.recipecompose.components.RecipeTopAppBar
import com.example.recipecompose.components.RecipeCard
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.ui.recipelist.FoodCategory
import com.example.recipecompose.ui.recipelist.components.FoodCategoryChip
import com.example.recipecompose.ui.recipelist.getAllFoodCategories
import dagger.hilt.android.AndroidEntryPoint

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
    val screens = listOf(Screen.Home, Screen.Other)
    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.onPrimary,
                elevation = 8.dp,
            ) {
                RecipeTopAppBar(
                    query = viewModel.query,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = viewModel::newSearch,
                    selectedCategory = viewModel.selectedCategory,
                    onSelectCategory = viewModel::onSelectedCategory
                )
            }
        },
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)

                screens.forEach { screen ->
                    val currentIcon = when (screen) {
                        Screen.Home -> Icons.Filled.Home
                        Screen.Other -> Icons.Filled.Favorite
                        else -> {
                            Icons.Filled.Home
                        }
                    }

                    BottomNavigationItem(
                        // Icon requires contentDescription
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
            composable(Screen.Home.route) {
                HomeScreen(
                    navController, // Replace this with lambda when parcelables are supported
                    viewModel.recipes,
                )
            }
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
fun HomeScreen(
    navController: NavController,
    recipes: List<Recipe>,
) {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(recipes) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = {
                    // Navigation must remain in callback to avoid being called during recomposition
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
