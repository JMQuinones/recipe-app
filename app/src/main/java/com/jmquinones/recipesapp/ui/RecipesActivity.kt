package com.jmquinones.recipesapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.jmquinones.recipesapp.databinding.ActivityMainBinding
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.data.repository.RecipesRepository
import com.jmquinones.recipesapp.utils.RecipesState
import kotlinx.coroutines.launch

class RecipesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var recipesViewModel: RecipesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recipesRepository = RecipesRepository()
        val viewModelProviderFactory = RecipesViewModelProviderFactory(application, recipesRepository)
        recipesViewModel = ViewModelProvider(this, viewModelProviderFactory)[RecipesViewModel::class.java]
        initUI()
    }

    private fun initUI() {
        initBottomNavigation()
    }



    private fun initBottomNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}