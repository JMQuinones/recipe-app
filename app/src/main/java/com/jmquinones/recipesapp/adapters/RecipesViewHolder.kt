package com.jmquinones.recipesapp.adapters

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmquinones.recipesapp.R
import com.jmquinones.recipesapp.databinding.RecipeItemBinding
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.utils.RecipesUtils

class RecipesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = RecipeItemBinding.bind(view)
    fun render(recipe: Recipe, onItemSelected: (Recipe) -> Unit) {
        val context: Context = binding.tvTitle.context
        binding.cvRecipe.setOnClickListener{
            onItemSelected(recipe)
        }
        binding.tvTitle.text = recipe.title
        Glide.with(binding.root).load(recipe.image).into(binding.ivRecipe)
        binding.tvDescription.text = recipe.description
        binding.tvAuthor.text = recipe.author.name
        binding.tvDetails.text = context.getString(
            R.string.details_placeholder,
            recipe?.details?.preparationTime?.let { RecipesUtils.timeToString(it.or(60)) },
            recipe?.details?.type,
            recipe?.details?.skillLevel
        )

    //binding.
    }

}