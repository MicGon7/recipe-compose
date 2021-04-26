package com.example.recipecompose.datastore

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import com.example.recipecompose.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SettingsDataStore @Inject constructor(app: BaseApplication) {
    val dataStore: DataStore<Preferences> = app.createDataStore(name = "settings")

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        observeDataStore()
    }

    var isDark by mutableStateOf(false)
        private set

    fun toggleTheme() {
        scope.launch {
            dataStore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY] ?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    private fun observeDataStore() {
        dataStore.data.onEach { preferences ->
            preferences[DARK_THEME_KEY]?.let { isDarkTheme ->
                isDark = isDarkTheme
            }

        }.launchIn(scope)
    }

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
    }
}