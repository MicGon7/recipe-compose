package com.example.recipecompose.presentation.home

sealed class HomeEvents {
    object NewSearchEvent : HomeEvents()
    object NextPageEvent : HomeEvents()
}