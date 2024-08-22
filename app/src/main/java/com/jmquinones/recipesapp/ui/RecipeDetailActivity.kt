package com.jmquinones.recipesapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.databinding.ActivityRecipeDetailBinding
import com.jmquinones.recipesapp.utils.RecipesUtils.Companion.getIngredients
import com.jmquinones.recipesapp.utils.RecipesUtils.Companion.recipeToRoomRecipe
import com.jmquinones.recipesapp.utils.RecipesUtils.Companion.timeToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity() {
    companion object{
        private const val TAG = "RecipeDetailActivity"
    }
    private lateinit var binding: ActivityRecipeDetailBinding
    //private lateinit var recipesViewModel: RecipesViewModel
    private val recipesViewModel by viewModels<RecipesViewModel>()


    private val args:RecipeDetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        loadData()

    }

    private fun initListeners() {
        if (args.isSaved) {
            binding.fabSaveRecipe.isVisible = false
            return
        }
        binding.fabSaveRecipe.setOnClickListener {
            val recipe = recipeToRoomRecipe(args.recipe)
            Log.d(TAG, "Recipe to save $recipe")
            recipesViewModel.saveRecipe(recipe)
            Snackbar.make(binding.root, "Receta Guardada", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun loadData() {
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

    private fun setUpIngredientsView(ingredients: List<String>){
        /*val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients)
        binding.lvIngredients.adapter = adapter*/
        val itemsString = ingredients.joinToString(separator = "\n\n")
        binding.tvIngredientsList.text = itemsString

    }
}