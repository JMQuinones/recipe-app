package com.jmquinones.recipesapp.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.R
import com.jmquinones.recipesapp.data.db.RecipesDatabase
import com.jmquinones.recipesapp.databinding.ActivityMainBinding
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.data.repository.RecipesRepository
import com.jmquinones.recipesapp.utils.Constants.Companion.DARK_MODE_KEY
import com.jmquinones.recipesapp.utils.RecipesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class RecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = RecipesDatabase(this)
        val recipesRepository = RecipesRepository(db)

        val viewModelProviderFactory =
            RecipesViewModelProviderFactory(application, recipesRepository)
        recipesViewModel =
            ViewModelProvider(this, viewModelProviderFactory)[RecipesViewModel::class.java]
        initUI()
    }

    private fun initUI() {
        loadTheme()
        initBottomNavigation()
    }

    private fun loadTheme() {
        val isDarkTheme = booleanPreferencesKey(DARK_MODE_KEY)
        val settingsFlow: Flow<Boolean> = this.dataStore.data
            .map { preferences ->
                preferences[isDarkTheme] ?: false
            }
        lifecycleScope.launch {
            settingsFlow.collect { setting ->
                if (setting) {
                    AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                delegate.applyDayNight()
            }
        }
    }


    private fun initBottomNavigation() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}