package com.example.recipecompose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.recipecompose.domain.model.Recipe
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 12.dp
    ) {
        Column {
            CoilImage(
                data = recipe.featuredImage,
                contentDescription = "Recipe Picture",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(225.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = recipe.title, modifier = Modifier
                        .fillMaxWidth(0.85f) // occupy entire width up to 85%
                        .wrapContentWidth(Alignment.Start),
                    style = MaterialTheme.typography.h5
                )
                Text(
                    text = recipe.rating.toString(), modifier = Modifier
                        .fillMaxWidth() // 15% because of title max width
                        .wrapContentWidth(Alignment.End)
                        .align(Alignment.CenterVertically), // rating text position is based on title
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}