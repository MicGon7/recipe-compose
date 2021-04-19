package com.example.recipecompose.data.cache.model

import androidx.room.*
import com.example.recipecompose.data.cache.converters.RecipeTypeConverters

@TypeConverters(RecipeTypeConverters::class)
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "publisher")
    val publisher: String,

    @ColumnInfo(name = "featured_image")
    val featuredImage: String,

    @ColumnInfo(name = "rating")
    val rating: Int,

    @ColumnInfo(name = "source_url")
    val sourceUrl: String,

    //SQL database cannot save a list. CSV Conversion required.
    /**
     * "carrots, cabbage, chicken"
     */
    @ColumnInfo(name = "ingredients")
    val ingredients: String,

    @ColumnInfo(name = "date_added")
    val dateAdded: Long,

    @ColumnInfo(name = "date_updated")
    val dateUpdated: Long,

    @ColumnInfo(name = "date_cached")
    val dateCached: Long,
)

