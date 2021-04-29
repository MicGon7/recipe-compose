package com.example.recipecompose.presentation.ui.recipedetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
fun RecipeItem(recipe: Recipe) {
    CoilImage(
        data = recipe.featuredImage,
        contentDescription = stringResource(R.string.recipe_image_cd),
        modifier = Modifier
            .height(250.dp),
        contentScale = ContentScale.Crop,
        error = {
            Image(
                painterResource(id = R.drawable.empty_plate),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )
        },
    )
    Column(
        modifier = Modifier.padding(8.dp),
    ) {
        Row {
            Text(
                text = recipe.title, modifier = Modifier
                    .fillMaxWidth(0.85f) // occupy entire width up to 85%
                    .wrapContentWidth(Alignment.Start),
                style = MaterialTheme.typography.h2
            )
            Text(
                text = recipe.rating.toString(), modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp) // 15% because of title max width
                    .wrapContentWidth(Alignment.End)
                    .align(Alignment.CenterVertically), // rating text position is based on title
                style = MaterialTheme.typography.h5
            )
        }
        val updated = recipe.dateUpdated
        Text(
            text = "Updated $updated by ${recipe.publisher}",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface.copy(alpha = .5f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        recipe.ingredients.forEach { ingredient ->
            Text(
                text = ingredient,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
