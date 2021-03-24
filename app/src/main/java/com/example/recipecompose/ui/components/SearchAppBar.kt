package com.example.recipecompose.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipecompose.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    selectedCategory: FoodCategory?,
    onSelectCategoryChange: (String) -> Unit,
    onSearch: () -> Unit,
    scrollPosition: Int,
    onScrollPositionChange: (Int) -> Unit
) {
    // Can also use LocalFocusManager which is not experimental (but this is future)
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column {
        Row(
            modifier = modifier

        ) {
            TextField(
                modifier = modifier
                    .fillMaxWidth(.9f)
                    .padding(8.dp),
                value = query,
                onValueChange = { newQuery -> onQueryChange(newQuery) },
                label = { Text(text = stringResource(R.string.search)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search//  Set (submit) icon to Magnify Glass
                ),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch()
                    keyboardController?.hideSoftwareKeyboard()
                }) {}
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            scope.launch {
                scrollState.scrollTo(scrollPosition)
            }

            for (category in getAllFoodCategories()) {
                FoodCategoryChip(
                    category = category.value,
                    isSelected = selectedCategory == category,
                    onSelected = {
                        onSelectCategoryChange(it)
                        onScrollPositionChange(scrollState.value)
                    },
                    onSearch = {
                        onSearch()
                    }
                )
            }
        }
    }
}
