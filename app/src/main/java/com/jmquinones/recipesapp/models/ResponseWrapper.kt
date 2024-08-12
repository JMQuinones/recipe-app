package com.jmquinones.recipesapp.models

import com.google.gson.annotations.SerializedName

data class ResponseWrapper(
    @SerializedName("content") val recipes: List<Recipe>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)