package com.jmquinones.recipesapp.ui.fragments.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jmquinones.recipesapp.R
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.utils.RecipesDiffUtil

class RecipesAdapter(private var recipesList: List<Recipe> = emptyList()): RecyclerView.Adapter<RecipesViewHolder>() {

    fun updateList(newList: List<Recipe>){
        // TODO Add diffcallback
        /*recipesList = list
        notifyDataSetChanged()*/

        val recipesDiffUtil = RecipesDiffUtil(recipesList, newList)
        val result = DiffUtil.calculateDiff(recipesDiffUtil)
        recipesList = newList
        result.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        return RecipesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
        )
    }

    override fun getItemCount() = recipesList.size

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.render(recipesList[position])
    }
}