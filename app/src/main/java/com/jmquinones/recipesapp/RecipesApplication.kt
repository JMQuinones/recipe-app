package com.jmquinones.recipesapp

import android.app.Application
import android.content.Context

class RecipesApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        RecipesApplication.appContext = applicationContext
    }

    companion object {

        lateinit  var appContext: Context

    }
}