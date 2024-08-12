package com.jmquinones.recipesapp.api

import com.jmquinones.recipesapp.models.ResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("api/recipe")
    suspend fun getAllRecipes(@Query("page") page:Int): Response<ResponseWrapper>

    @GET("api/recipe/hello")
    suspend fun sayHello(): Response<String>

}