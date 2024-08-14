package com.jmquinones.recipesapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jmquinones.recipesapp.R
import com.jmquinones.recipesapp.data.paging.RecipePagingSource
import com.jmquinones.recipesapp.data.repository.RecipesRepository
import com.jmquinones.recipesapp.databinding.RecipeItemBinding
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.utils.RecipeDetailsUtils

class RecipesPagingAdapter(private val onItemSelected:(Recipe) -> Unit) :
    PagingDataAdapter<Recipe, RecipesPagingAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemSelected)
    }

    class ItemViewHolder(private val binding: RecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe?, onItemSelected:(Recipe) -> Unit) {
            val context: Context = binding.tvTitle.context
            binding.cvRecipe.setOnClickListener {
                onItemSelected(recipe!!)
            }
            binding.tvTitle.text = recipe?.title
            Glide.with(binding.root).load(recipe?.image).into(binding.ivRecipe)
            binding.tvDescription.text = recipe?.description
            binding.tvAuthor.text = recipe?.author?.name
            binding.tvDetails.text = context.getString(
                R.string.details_placeholder,
                recipe?.details?.preparationTime?.let { RecipeDetailsUtils.timeToString(it.or(60)) },
                recipe?.details?.type,
                recipe?.details?.skillLevel
            )

        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
                return oldItem == newItem
            }
        }
    }
}