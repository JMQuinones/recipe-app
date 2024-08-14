package com.jmquinones.recipesapp.data.repository

import com.jmquinones.recipesapp.data.api.RetrofitInstance
import com.jmquinones.recipesapp.models.ResponseWrapper
import retrofit2.Response

class RecipesRepository {
    suspend fun getAllRecipes(page: Int, pageSize: Int): Response<ResponseWrapper> {
        return RetrofitInstance.api.getAllRecipes(page, pageSize)
    }

    suspend fun sayHello(): Response<String> {
        return RetrofitInstance.api.sayHello();
    }
}