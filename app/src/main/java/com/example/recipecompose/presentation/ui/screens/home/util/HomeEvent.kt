package com.example.recipecompose.presentation.ui.screens.home.util

sealed class HomeEvent {
    object NewSearchEvent : HomeEvent()
    object NextPageEvent : HomeEvent()
    object RestoreStateEvent : HomeEvent()
}