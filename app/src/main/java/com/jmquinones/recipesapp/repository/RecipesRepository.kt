package com.jmquinones.recipesapp.repository

import com.jmquinones.recipesapp.api.RetrofitInstance
import com.jmquinones.recipesapp.models.ResponseWrapper
import retrofit2.Response

class RecipesRepository {
    suspend fun getAllRecipes(page: Int): Response<ResponseWrapper> {
        return RetrofitInstance.api.getAllRecipes(page)
    }

    suspend fun sayHello(): Response<String> {
        return RetrofitInstance.api.sayHello();
    }
}