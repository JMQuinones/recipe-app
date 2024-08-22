package com.jmquinones.recipesapp.models

import java.io.Serializable

data class Author(
    val about: String,
    val name: String,
    val photo: String
): Serializable