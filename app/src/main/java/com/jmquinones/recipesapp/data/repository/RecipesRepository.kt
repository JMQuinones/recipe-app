package com.jmquinones.recipesapp.data.repository

import com.jmquinones.recipesapp.data.api.RetrofitInstance
import com.jmquinones.recipesapp.data.db.RecipesDatabase
import com.jmquinones.recipesapp.models.RecipeRoom
import com.jmquinones.recipesapp.models.ResponseWrapper
import retrofit2.Response

class RecipesRepository(val db: RecipesDatabase) {
    suspend fun getAllRecipes(page: Int, pageSize: Int): Response<ResponseWrapper> {
        return RetrofitInstance.api.getAllRecipes(page, pageSize)
    }

    suspend fun searchByTitleOrAuthor(page: Int, pageSize: Int, query: String): Response<ResponseWrapper> {
        return RetrofitInstance.api.searchByTitleOrAuthor(page, pageSize, query)
    }

    suspend fun sayHello(): Response<String> {
        return RetrofitInstance.api.sayHello();
    }

    suspend fun upsertRecipe(recipe: RecipeRoom) =
        db.getRecipesDao().upsert(recipe)

    fun getSavedRecipes() =
        db.getRecipesDao().getAllRecipes()

    suspend fun deleteRecipe(recipe: RecipeRoom) =
        db.getRecipesDao().delete(recipe)


}