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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.paging.filter
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.R
import com.jmquinones.recipesapp.databinding.FragmentRecipesBinding
import com.jmquinones.recipesapp.models.ResponseWrapper
import com.jmquinones.recipesapp.ui.RecipesActivity
import com.jmquinones.recipesapp.ui.RecipesViewModel
import com.jmquinones.recipesapp.adapters.RecipesAdapter
import com.jmquinones.recipesapp.adapters.RecipesPagingAdapter
import com.jmquinones.recipesapp.utils.RecipesState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class RecipesFragment : Fragment() {
    companion object {
        val TAG = "RecipesFragment"
    }

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var pagingAdapter: RecipesPagingAdapter

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
        //setupRecyclerview()
        setupPagingRecyclerView()
        initListeners()
        recipesViewModel = (activity as RecipesActivity).recipesViewModel
        getRecipes()


    }

    private fun initListeners() {
        binding.btnRetry.setOnClickListener {
            pagingAdapter.retry()
        }
    }

    private fun getRecipes() {
        //recipesViewModel = ViewModelProvider(this)[RecipesViewModel::class.java]
        viewLifecycleOwner.lifecycleScope.launch {
            recipesViewModel.recipes.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
        //loadRecipes(view)
    }

    private fun setupPagingRecyclerView(){
        pagingAdapter = RecipesPagingAdapter(onItemSelected = { recipe ->
            findNavController().navigate(
                RecipesFragmentDirections.actionRecipesFragmentToRecipeDetailActivity(recipe)
            )
        })
        pagingAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {  // refresh is for the first load, prepend and append are for subsequent loads
                is LoadState.Loading -> {
                    // Show loading spinner
                    binding.firstLoadProgressBar.visibility = View.VISIBLE
                    binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    // Hide loading spinner
                    binding.firstLoadProgressBar.visibility = View.GONE
                    binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                }
                is LoadState.Error -> {
                    // Hide loading spinner and show an error message
                    binding.firstLoadProgressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.VISIBLE
                    val error = (loadState.refresh as LoadState.Error).error
                    showError(error)
                }
            }
            when (loadState.append) {
                is LoadState.Loading -> {
                    binding.paginationProgressBar.visibility = View.VISIBLE
                    /*binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE*/
                }
                is LoadState.NotLoading -> {
                    binding.paginationProgressBar.visibility = View.GONE
                    /*binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE*/
                }
                is LoadState.Error -> {
                    // Show error message and allow retry
                    val error = (loadState.prepend as LoadState.Error).error
                    binding.paginationProgressBar.visibility = View.GONE
                    binding.tvError.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.VISIBLE
                    showError(error)
                }
            }
        }
        binding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter
        }
    }

    private fun showError(error: Throwable) {
        error.printStackTrace()
        Snackbar.make(requireView(), getString(R.string.loading_error), Snackbar.LENGTH_LONG).show()
    }

    /*private fun setupRecyclerview() {
        recipesAdapter = RecipesAdapter(onItemSelected = { recipe ->
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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                recipesViewModel.state.collect {
                    when (it) {
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
        Snackbar.make(
            requireView(),
            "Se produjo un error: ${recipesState.error}",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleSuccess(response: ResponseWrapper) {
        recipesAdapter.updateList(response.recipes)
        controlProgressBar(false)
    }

    private fun controlProgressBar(isVisible: Boolean) {
        binding.paginationProgressBar.isVisible = isVisible
    }*/
}