package com.jmquinones.recipesapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.jmquinones.recipesapp.models.RecipeRoom

@Database(entities = [RecipeRoom::class],version = 1)
@TypeConverters(Converters::class)
abstract class RecipesDatabase: RoomDatabase() {
    abstract fun getRecipesDao(): RecipesDao

    companion object {
        @Volatile
        private var instance: RecipesDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RecipesDatabase::class.java,
                "recipes_db.db"
            ).build()
    }

}