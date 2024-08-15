package com.jmquinones.recipesapp.data.api

import com.jmquinones.recipesapp.models.ResponseWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {
    @GET("api/recipe")
    suspend fun getAllRecipes(
        @Query("page") page: Int,
        @Query("size") pageSize: Int
    ): Response<ResponseWrapper>

    @GET("api/recipe/hello")
    suspend fun sayHello(): Response<String>

    @GET("api/recipe/search")
    suspend fun searchByTitleOrAuthor(
        @Query("page") page: Int,
        @Query("size") pageSize: Int,
        @Query("query") query: String
    ): Response<ResponseWrapper>

}