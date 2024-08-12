package com.jmquinones.recipesapp.ui.fragments.recipes.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmquinones.recipesapp.databinding.RecipeItemBinding
import com.jmquinones.recipesapp.models.Recipe

class RecipesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val binding = RecipeItemBinding.bind(view)
    fun render(recipe: Recipe) {
        val context: Context = binding.tvTitle.context

        binding.tvTitle.text = recipe.title
        Glide.with(binding.root).load(recipe.image).into(binding.ivRecipe)
        binding.tvDescription.text = recipe.description
        binding.tvAuthor.text = recipe.author.name
        binding.tvDetails.text = "${recipe.details.preparationTime} - ${recipe.details.type} - ${recipe.details.skillLevel}"
        //binding.
    }

}