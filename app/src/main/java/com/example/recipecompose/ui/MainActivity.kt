package com.example.recipecompose.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.recipecompose.BaseApplication
import com.example.recipecompose.domain.model.Recipe
import com.example.recipecompose.ui.components.*
import com.example.recipecompose.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
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

    Scaffold(
        topBar = {
            SearchAppBar(
                query = viewModel.query,
                onQueryChange = viewModel::onQueryChange,
                selectedCategory = viewModel.selectedCategory,
                onSelectCategoryChange = viewModel::onSelectedCategoryChange,
                onSearch = viewModel::newSearch,
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
        drawerContent = { NavDrawer() }
    ) {
        NavHost(navController, startDestination = Screen.Home.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController, // Replace this with lambda when parcelables are supported
                    viewModel.recipes,
                    viewModel.loading
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
    loading: Boolean
) {
    // Box allows for overlay of composables--last composables shown on top
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 52.dp) // place above bottom bar
    ) {
        if (loading) {
            LoadingRecipeList(imageSize = 250.dp)
        } else {
            LazyColumn {
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
        CircularIndeterminateProgressBar(isDisplayed = loading, verticalBias = 0.4f)
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

// Feature UI Demo Composables
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

@Composable
fun NavDrawer() {
    Column() {
        Text(text = "Item 1")
        Text(text = "Item 2")
        Text(text = "Item 3")
        Text(text = "Item 4")
        Text(text = "Item 5")
    }
}