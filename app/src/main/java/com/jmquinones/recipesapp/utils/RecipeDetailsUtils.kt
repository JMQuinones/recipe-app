package com.jmquinones.recipesapp.utils

import java.math.BigDecimal
import java.math.RoundingMode

class RecipeDetailsUtils {
    companion object{
        fun timeToString(time: Int): String {
            if (time <= 60){
                return "$time min"
            }
            val hours = time.toDouble()/60
            val rounded = BigDecimal(hours).setScale(1, RoundingMode.HALF_EVEN).toString()
            return rounded.plus(" hrs")
        }

        fun getIngredients(ingredients: String): List<String>{
            var list = ingredients.split('|');
            list = list.map { item ->
                "\u2022 $item"
            }
            return list
        }
    }
}