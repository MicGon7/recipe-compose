package com.example.recipecompose.ui.home

sealed class HomeScreenEvents {
    object NewSearchEvent : HomeScreenEvents()
    object NextPageEvent : HomeScreenEvents()
}