package com.jmquinones.recipesapp.data.repository

import com.jmquinones.recipesapp.data.api.RecipeApi
import com.jmquinones.recipesapp.data.db.RecipesDatabase
import com.jmquinones.recipesapp.models.RecipeRoom
import com.jmquinones.recipesapp.models.ResponseWrapper
import retrofit2.Response
import javax.inject.Inject

class RecipesRepository @Inject constructor(private val db: RecipesDatabase, private val recipeApi: RecipeApi) {
    suspend fun getAllRecipes(page: Int, pageSize: Int): Response<ResponseWrapper> {
        return recipeApi.getAllRecipes(page, pageSize)
    }

    suspend fun searchByTitleOrAuthor(page: Int, pageSize: Int, query: String): Response<ResponseWrapper> {
        return recipeApi.searchByTitleOrAuthor(page, pageSize, query)
    }

    suspend fun sayHello(): Response<String> {
        return recipeApi.sayHello();
    }

    suspend fun upsertRecipe(recipe: RecipeRoom) =
        db.getRecipesDao().upsert(recipe)

    fun getSavedRecipes() =
        db.getRecipesDao().getAllRecipes()

    suspend fun deleteRecipe(recipe: RecipeRoom) =
        db.getRecipesDao().delete(recipe)


}