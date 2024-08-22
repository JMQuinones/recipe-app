package com.jmquinones.recipesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jmquinones.recipesapp.models.RecipeRoom

@Database(entities = [RecipeRoom::class],version = 1)
@TypeConverters(Converters::class)
abstract class RecipesDatabase: RoomDatabase() {
    abstract fun getRecipesDao(): RecipesDao
}