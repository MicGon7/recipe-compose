package com.example.recipecompose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.recipecompose.R
import com.example.recipecompose.domain.model.Recipe
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit, preview: Boolean = false) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 12.dp
    ) {
        Column {
            val imageHeight = 225.dp

            recipe.featuredImage?.let { url ->
                // TODO: Remove preview check when Coil adds support for the preview feature.
                CoilImage(
                    data = url,
                    contentDescription = stringResource(R.string.recipe_image_cd),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    contentScale = ContentScale.Crop,
                    error = {
                        Image(
                            painterResource(id = R.drawable.empty_plate),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                        )
                    },
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = recipe.title, modifier = Modifier
                        .fillMaxWidth(0.85f) // occupy entire width up to 85%
                        .wrapContentWidth(Alignment.Start),
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = recipe.rating.toString(), modifier = Modifier
                        .fillMaxWidth() // 15% because of title max width
                        .wrapContentWidth(Alignment.End)
                        .align(Alignment.CenterVertically), // rating text position is based on title
                    style = MaterialTheme.typography.h5
                )
            }
        }
    }
}