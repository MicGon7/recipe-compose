package com.example.recipecompose.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * See Recipe example: https://food2fork.ca/
 */
@Parcelize
data class Recipe (
    val id: Int,
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int,
    val sourceUrl: String,
    val ingredients: List<String>,
    val dateAdded: String,
    val dateUpdated: String,
): Parcelable