package com.jmquinones.recipesapp.ui.fragments.saved

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.adapters.RecipesAdapter
import com.jmquinones.recipesapp.databinding.FragmentSavedBinding
import com.jmquinones.recipesapp.ui.RecipesViewModel
import com.jmquinones.recipesapp.utils.RecipesUtils.Companion.recipeRoomToRecipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedFragment : Fragment() {

    private lateinit var recipesAdapter: RecipesAdapter
    private val recipesViewModel by viewModels<RecipesViewModel>()
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSavedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                val recipe = recipesAdapter.recipesList[position]
                recipesViewModel.deleteRecipe(recipe)
                val recipeList = recipesAdapter.recipesList.minus(recipe)
                recipesAdapter.updateList(recipeList)
                Snackbar.make(view, "Receta eliminada", Snackbar.LENGTH_LONG).apply {
                    setAction("Deshacer"){
                        recipesViewModel.saveRecipe(recipe)
                    }
                    show()
                }
            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedRecipes)
        }
    }

    private fun initUI() {
        initRecyclerView()
        loadSavedRecipes()
    }

    private fun loadSavedRecipes() {
        recipesViewModel.savedRecipes.observe(viewLifecycleOwner) { recipes ->
            recipesAdapter.updateList(recipes)
        }

    }

    private fun initRecyclerView() {
        recipesAdapter = RecipesAdapter(onItemSelected = { recipeRoom ->
            val recipe = recipeRoomToRecipe(recipeRoom)
            findNavController().navigate(
                SavedFragmentDirections.actionSavedFragmentToRecipeDetailActivity(recipe, true)
            )
        })
        binding.rvSavedRecipes.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}