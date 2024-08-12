package com.jmquinones.recipesapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.databinding.FragmentRecipesBinding
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.models.ResponseWrapper
import com.jmquinones.recipesapp.repository.RecipesRepository
import com.jmquinones.recipesapp.ui.RecipesActivity
import com.jmquinones.recipesapp.ui.RecipesViewModel
import com.jmquinones.recipesapp.utils.RecipesState
import kotlinx.coroutines.launch


class RecipesFragment : Fragment() {
    companion object{
        val TAG = "RecipesFragment"
    }
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

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
        recipesViewModel = (activity as RecipesActivity).recipesViewModel
        loadRecipes(view)
    }

    private fun loadRecipes(view: View) {
        val recipesRepository = RecipesRepository()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                recipesViewModel.state.collect{
                    when(it) {
                        is RecipesState.Error -> Snackbar.make(view, "Se produjo un error: ${it.error}", Snackbar.LENGTH_LONG)
                        is RecipesState.Loading -> Snackbar.make(view, "Cargando recetas ...", Snackbar.LENGTH_LONG)
                        is RecipesState.Success -> handleSuccess(it.response)
                    }
                }
            }
        }
    }

    private fun handleSuccess(response: ResponseWrapper) {
        Log.d(TAG, response.recipes.toString())
    }
}