package com.jmquinones.recipesapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.jmquinones.recipesapp.models.RecipeRoom

class RecipesDiffUtil(
    private val oldList: List<RecipeRoom>,
    private val newList: List<RecipeRoom>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}