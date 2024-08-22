package com.jmquinones.recipesapp.utils

import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.models.RecipeRoom
import java.math.BigDecimal
import java.math.RoundingMode

class RecipesUtils {
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

        fun recipeToRoomRecipe(recipe: Recipe): RecipeRoom{
            return RecipeRoom(
                recipe.author,
                recipe.description,
                recipe.details,
                recipe.image,
                recipe.ingredients,
                recipe.instructions,
                recipe.title,
            )
        }

        fun recipeRoomToRecipe(recipeRoom: RecipeRoom): Recipe{
            return Recipe(
                recipeRoom.id,
                recipeRoom.author,
                recipeRoom.description,
                recipeRoom.details,
                recipeRoom.image,
                recipeRoom.ingredients,
                recipeRoom.instructions,
                recipeRoom.title,
            )
        }
    }
}