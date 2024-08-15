package com.jmquinones.recipesapp.ui.fragments.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.paging.LOG_TAG
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmquinones.recipesapp.adapters.RecipesPagingAdapter
import com.jmquinones.recipesapp.databinding.FragmentSearchBinding
import com.jmquinones.recipesapp.ui.RecipesActivity
import com.jmquinones.recipesapp.ui.RecipesViewModel
import com.jmquinones.recipesapp.ui.fragments.recipes.RecipesFragmentDirections
import com.jmquinones.recipesapp.utils.Constants.Companion.SEARCH_DELAY
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {
    companion object {
        const val TAG = "SearchFragment"
    }
    private lateinit var pagingAdapter: RecipesPagingAdapter
    private lateinit var recipesViewModel: RecipesViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPagingRecyclerView()
        recipesViewModel = (activity as RecipesActivity).recipesViewModel
        initListeners()
    }

    private fun initListeners() {
        var job: Job?= null
        binding.etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        binding.tvNotFound.isVisible = false
                        recipesViewModel.getSearchResults(editable.toString()).collectLatest { recipes ->
                            pagingAdapter.submitData(recipes)
                        }
                    } else {
                        pagingAdapter.submitData(PagingData.empty())
                        binding.tvNotFound.isVisible = true
                    }
                }
            }
        }
    }

    private fun setupPagingRecyclerView() {
        pagingAdapter = RecipesPagingAdapter(onItemSelected = { recipe ->
            Log.d(TAG, recipe.title)
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToRecipeDetailActivity(recipe)
            )
        })
        binding.rvSearchedRecipes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter
        }
    }


}