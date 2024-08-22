package com.jmquinones.recipesapp.models

import java.io.Serializable

data class Details(
    val preparationTime: Int,
    val servings: Int,
    val skillLevel: String,
    val type: String
): Serializable