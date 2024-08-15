package com.jmquinones.recipesapp.ui.fragments.saved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.jmquinones.recipesapp.adapters.RecipesAdapter
import com.jmquinones.recipesapp.databinding.FragmentSavedBinding
import com.jmquinones.recipesapp.ui.RecipesActivity
import com.jmquinones.recipesapp.ui.RecipesViewModel

class SavedFragment : Fragment() {

    private lateinit var recipesAdapter: RecipesAdapter
    private lateinit var recipesViewModel: RecipesViewModel

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
        recipesViewModel = (activity as RecipesActivity).recipesViewModel

        initUI()
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
        recipesAdapter = RecipesAdapter(onItemSelected = {
            Toast.makeText(requireContext(), "Recipe: ${it.title}", Toast.LENGTH_SHORT).show()
        })
        binding.rvSavedRecipes.apply {
            adapter = recipesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}