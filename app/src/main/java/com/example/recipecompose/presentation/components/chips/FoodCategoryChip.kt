package com.example.recipecompose.presentation.components.chips

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.recipecompose.presentation.ui.screens.home.util.HomeEvent
import kotlinx.coroutines.launch

@Composable
fun FoodCategoryChip(
    category: String,
    isSelected: Boolean = false,
    onSelected: (String) -> Unit,
    onToggleEvent: (HomeEvent) -> Unit,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    Surface(
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 4.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.LightGray else MaterialTheme.colors.primary
    ) {
        Row(
            modifier = Modifier
                .toggleable(
                    value = isSelected,
                    onValueChange = {
                        onSelected(category)
                        if (category == "Milk") {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = "Invalid Category: MILK!",
                                    actionLabel = "Hide"
                                )
                            }
                        } else {
                            onToggleEvent(HomeEvent.NewSearchEvent)
                        }
                    })
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.button,
                color = Color.White,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}