package com.jmquinones.recipesapp.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.databinding.FragmentRecipesBinding
import com.jmquinones.recipesapp.models.ResponseWrapper
import com.jmquinones.recipesapp.ui.RecipesActivity
import com.jmquinones.recipesapp.ui.RecipesViewModel
import com.jmquinones.recipesapp.adapters.RecipesAdapter
import com.jmquinones.recipesapp.utils.RecipesState
import kotlinx.coroutines.launch


class RecipesFragment : Fragment() {
    companion object{
        val TAG = "RecipesFragment"
    }
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipesAdapter: RecipesAdapter

    //private val recipesViewModel by viewModels<RecipesViewModel>()
    private lateinit var recipesViewModel: RecipesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerview()
        recipesViewModel = (activity as RecipesActivity).recipesViewModel
        loadRecipes(view)
    }

    private fun setupRecyclerview() {
        recipesAdapter = RecipesAdapter(onItemSelected = { recipe ->
            Toast.makeText(context, recipe.title, Toast.LENGTH_LONG).show()
            findNavController().navigate(
                RecipesFragmentDirections.actionRecipesFragmentToRecipeDetailActivity(recipe)
            )
        })

        binding.rvRecipes.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadRecipes(view: View) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                recipesViewModel.state.collect{
                    when(it) {
                        is RecipesState.Error -> handleError(it)
                        is RecipesState.Loading -> controlProgressBar(true)
                        is RecipesState.Success -> handleSuccess(it.response)
                    }
                }
            }
        }
    }

    private fun handleError(recipesState: RecipesState.Error) {
        controlProgressBar(false)
        Log.e(TAG, recipesState.error)
        Snackbar.make(requireView(), "Se produjo un error: ${recipesState.error}", Snackbar.LENGTH_LONG).show()
    }

    private fun handleSuccess(response: ResponseWrapper) {
        recipesAdapter.updateList(response.recipes)
        controlProgressBar(false)
    }

    private fun controlProgressBar(isVisible: Boolean){
        binding.paginationProgressBar.isVisible = isVisible
    }
}