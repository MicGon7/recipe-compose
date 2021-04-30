package com.example.recipecompose.presentation.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipecompose.R
import com.example.recipecompose.datastore.SettingsDataStore
import com.example.recipecompose.presentation.components.chips.FoodCategoryChip
import com.example.recipecompose.presentation.ui.screens.home.HomeViewModel
import com.example.recipecompose.presentation.ui.screens.home.util.getAllFoodCategories
import com.example.recipecompose.presentation.ui.screens.home.util.HomeEvent
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(
    homeViewModel: HomeViewModel = viewModel(),
    settingsDataStore: SettingsDataStore,
    scaffoldState: ScaffoldState
) {

    // Can also use LocalFocusManager which is not experimental (but this is future)
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.secondary,
        elevation = 12.dp
    ) {
        Column {
            Row {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .padding(8.dp),
                    value = homeViewModel.query,
                    onValueChange = { newQuery -> homeViewModel.onQueryChange(newQuery) },
                    label = { Text(text = stringResource(R.string.search)) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search//  Set (submit) icon to Magnify Glass
                    ),
                    keyboardActions = KeyboardActions(onSearch = {
                        homeViewModel.onTriggerEvent(HomeEvent.NewSearchEvent)
                        keyboardController?.hideSoftwareKeyboard()
                    }) {}
                )
                IconButton(
                    onClick = { settingsDataStore.toggleTheme() },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 8.dp)
                ) {
                    Icon(Icons.Filled.MoreVert, contentDescription = null)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                scope.launch {
                    scrollState.scrollTo(homeViewModel.categoryScrollPosition)
                }
                for (category in getAllFoodCategories()) {
                    FoodCategoryChip(
                        category = category.value,
                        isSelected = homeViewModel.selectedCategory == category,
                        onSelected = {
                            homeViewModel.onSelectedCategoryChange(it)
                            homeViewModel.onScrollPositionChange(scrollState.value)
                        },
                        onToggleEvent = { homeViewModel.onTriggerEvent(HomeEvent.NewSearchEvent) },
                        scaffoldState = scaffoldState
                    )
                }
            }
        }
    }
}