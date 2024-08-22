package com.jmquinones.recipesapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jmquinones.recipesapp.models.RecipeRoom

@Dao
interface RecipesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(recipe: RecipeRoom)

    @Delete
    suspend fun delete(recipe: RecipeRoom)

    @Query("SELECT * FROM recipes ORDER BY id ASC" )
    fun getAllRecipes(): LiveData<List<RecipeRoom>>

}