package com.example.recipecompose.presentation.ui.home

sealed class HomeEvent {
    object NewSearchEvent : HomeEvent()
    object NextPageEvent : HomeEvent()
    object RestoreStateEvent : HomeEvent()
}