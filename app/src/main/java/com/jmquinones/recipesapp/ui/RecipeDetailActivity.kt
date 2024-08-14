package com.jmquinones.recipesapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.jmquinones.recipesapp.databinding.ActivityRecipeDetailBinding
import com.jmquinones.recipesapp.utils.RecipeDetailsUtils.Companion.getIngredients
import com.jmquinones.recipesapp.utils.RecipeDetailsUtils.Companion.timeToString
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Locale

class RecipeDetailActivity : AppCompatActivity() {
    companion object{
        private val TAG = "RecipeDetailActivity"
    }
    private lateinit var binding: ActivityRecipeDetailBinding
    private val args:RecipeDetailActivityArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        val recipe = args.recipe
        val prepTime = timeToString(recipe.details.preparationTime)
        //val prepTime = timeToString(452)

        val ingredients = getIngredients(recipe.ingredients)
        setUpIngredientsView(ingredients)

        binding.tvTitle.text = recipe.title
        binding.tvDescription.text = recipe.description
        binding.tvAuthor.text = recipe.author.name
        binding.tvSkill.text = recipe.details.skillLevel
        binding.tvServings.text = recipe.details.servings.toString().plus(" porciones")
        binding.tvTime.text = prepTime
//        binding.tvTime.text = timeToString(recipe.details.preparationTime)
        binding.tvRecipeSteps.text = recipe.instructions
        Glide.with(binding.root).load(recipe.image).into(binding.ivFood)

    }

    /*private fun timeToString(time: Int): String {
        if (time <= 60){
            return "$time min"
        }
        val hours = time.toDouble()/60
        val rounded = BigDecimal(hours).setScale(1, RoundingMode.HALF_EVEN).toString()
        return rounded.plus(" hrs")
    }

    private fun getIngredients(ingredients: String): List<String>{
        var list = ingredients.split('|');
        list = list.map { item ->
            "\u2022 $item"
        }
        return list
    }*/

    private fun setUpIngredientsView(ingredients: List<String>){
        /*val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients)
        binding.lvIngredients.adapter = adapter*/
        val itemsString = ingredients.joinToString(separator = "\n\n")
        binding.tvIngredientsList.text = itemsString

    }
}