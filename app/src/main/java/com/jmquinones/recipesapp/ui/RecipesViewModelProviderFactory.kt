package com.jmquinones.recipesapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmquinones.recipesapp.data.repository.RecipesRepository

class RecipesViewModelProviderFactory(val app: Application, val recipesRepository: RecipesRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipesViewModel(recipesRepository, app) as T
    }
}