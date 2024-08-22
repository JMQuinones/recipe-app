package com.jmquinones.recipesapp.ui.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jmquinones.recipesapp.R
import com.jmquinones.recipesapp.adapters.RecipesPagingAdapter
import com.jmquinones.recipesapp.databinding.FragmentSearchBinding
import com.jmquinones.recipesapp.ui.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {
    companion object {
        const val TAG = "SearchFragment"
    }

    private lateinit var pagingAdapter: RecipesPagingAdapter
    private val recipesViewModel by viewModels<RecipesViewModel>()
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
        initUI()
        //initListeners()
    }

    private fun initUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            recipesViewModel.searchRecipes.collectLatest { pagingData ->
                pagingAdapter.submitData(lifecycle, pagingData)
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                val isListEmpty = pagingAdapter.itemCount == 0
                Log.d(TAG, "Is list empty $isListEmpty")
                Log.d(TAG, "Load state ${loadStates.refresh}")
                if ((loadStates.refresh is LoadState.NotLoading || loadStates.refresh is LoadState.Error) && isListEmpty) {
                    Log.d(TAG, "Inside true")

                    binding.tvNotFound.visibility = View.VISIBLE
                    binding.rvSearchedRecipes.visibility = View.GONE
                } else {
                    Log.d(TAG, "Inside else")

                    binding.tvNotFound.visibility = View.GONE
                    binding.rvSearchedRecipes.visibility = View.VISIBLE
                }
            }
        }

        initListeners()
    }

    private fun initListeners() {
        binding.btnSearch.setOnClickListener {
            pagingAdapter.submitData(lifecycle, PagingData.empty())
            val query = binding.etSearch.text.toString()
            recipesViewModel.setQuery(query)
        }
        pagingAdapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.searchProgressBar.visibility = View.VISIBLE
                    binding.tvNotFound.visibility = View.GONE
                }

                is LoadState.NotLoading -> {
                    binding.searchProgressBar.visibility = View.GONE
                    binding.tvNotFound.visibility = View.GONE
                }

                is LoadState.Error -> {
                    binding.searchProgressBar.visibility = View.GONE
                    val error = (loadState.refresh as LoadState.Error).error
                    showError(error)
                }
            }
        }
    }

    private fun showError(error: Throwable) {
        error.printStackTrace()
        val message =
            Snackbar.make(
                requireView(),
                getString(
                    R.string.loading_error,
                    error.message.orEmpty().plus(getString(R.string.empty_error))
                ),
                Snackbar.LENGTH_LONG
            ).show()

    }

    // TODO Implement reactive search
    /*private fun initListeners() {
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
    }*/

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