package com.example.recipecompose.database

import androidx.room.*
import com.example.recipecompose.domain.model.Recipe

@TypeConverters(RecipeTypeConverters::class)
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val publisher: String,
    @ColumnInfo(name = "featured_image")
    val featuredImage: String?,
    val rating: Int,
    @ColumnInfo(name = "source_url")
    val sourceUrl: String,
    val ingredients: List<String>,
    @ColumnInfo(name = "date_added")
    val dateAdded: String,
    @ColumnInfo(name = "date_updated")
    val dateUpdated: String,
) {
    companion object {
        fun from(recipe: Recipe): RecipeEntity {
            return RecipeEntity(
                id = recipe.id,
                title = recipe.title,
                publisher = recipe.publisher,
                featuredImage = recipe.featuredImage,
                rating = recipe.rating,
                sourceUrl = recipe.sourceUrl,
                ingredients = recipe.ingredients,
                dateAdded = recipe.dateAdded,
                dateUpdated = recipe.dateUpdated
            )
        }

        fun toRecipe(recipeEntity: RecipeEntity): Recipe {
            return Recipe(
                id = recipeEntity.id,
                title = recipeEntity.title,
                publisher = recipeEntity.publisher,
                featuredImage = recipeEntity.featuredImage,
                rating = recipeEntity.rating,
                sourceUrl = recipeEntity.sourceUrl,
                ingredients = recipeEntity.ingredients,
                dateAdded = recipeEntity.dateAdded,
                dateUpdated = recipeEntity.dateUpdated
            )
        }

    }
}
