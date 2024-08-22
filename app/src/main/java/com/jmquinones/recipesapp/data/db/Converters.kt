package com.jmquinones.recipesapp.data.db

import androidx.room.TypeConverter
import com.jmquinones.recipesapp.models.Author
import com.jmquinones.recipesapp.models.Details
import org.json.JSONObject

class Converters {
    @TypeConverter
    fun fromAuthor(author: Author): String {
        return JSONObject().apply {
            put("about", author.about)
            put("name", author.name)
            put("photo", author.photo)
        }.toString()
    }

    @TypeConverter
    fun toAuthor(author: String): Author {
        val json = JSONObject(author)
        return Author(json.getString("about"), json.getString("name"), json.getString("photo"))
    }

    @TypeConverter
    fun fromDetails(details: Details): String {
        return JSONObject().apply {
            put("preparationTime", details.preparationTime)
            put("servings", details.servings)
            put("skillLevel", details.skillLevel)
            put("type", details.type)
        }.toString()
    }

    @TypeConverter
    fun toDetails(details: String): Details {
        val json = JSONObject(details)
        return Details(
            json.getInt("servings"),
            json.getInt("servings"),
            json.getString("skillLevel"), json.getString("type")
        )
    }
}