package com.jmquinones.recipesapp.utils

import com.jmquinones.recipesapp.models.ResponseWrapper

sealed class RecipesState {
    data object Loading : RecipesState()
    data class Error(val error: String) : RecipesState()
    data class Success(
        val response: ResponseWrapper
    ) : RecipesState()
}