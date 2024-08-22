package com.jmquinones.recipesapp.models

import java.io.Serializable

data class Recipe(
    val id: Int,
    val author: Author,
    val description: String,
    val details: Details,
    val image: String,
    val ingredients: String,
    val instructions: String,
    val title: String
): Serializable