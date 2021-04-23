package com.example.recipecompose.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import com.example.recipecompose.BaseApplication
import com.example.recipecompose.RecipeApp
import com.example.recipecompose.presentation.util.CustomConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Temp to demo light/dark theme
    @Inject
    lateinit var baseApplication: BaseApplication

    @Inject
    lateinit var connectivityManager: CustomConnectivityManager

    override fun onStart() {
        super.onStart()
        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterConnectionObserver(this)
    }

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeApp(
                baseApplication = baseApplication,
                isNetworkAvailable = connectivityManager.isNetworkAvailable.value
            )
        }
    }
}
