package com.jmquinones.recipesapp.di

import android.content.Context
import androidx.room.Room
import com.jmquinones.recipesapp.data.db.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): RecipesDatabase {
        return Room.databaseBuilder(
            appContext,
            RecipesDatabase::class.java,
            "recipes_db.db"
        ).build()
    }
}