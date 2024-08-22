package com.jmquinones.recipesapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jmquinones.recipesapp.data.paging.RecipePagingSource
import com.jmquinones.recipesapp.data.paging.SearchRecipePagingSource
import com.jmquinones.recipesapp.data.repository.RecipesRepository
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.models.RecipeRoom
import com.jmquinones.recipesapp.utils.Constants.Companion.PAGE_SIZE
import com.jmquinones.recipesapp.utils.RecipesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(private val recipesRepository: RecipesRepository) :
    ViewModel() {
    private var _state = MutableStateFlow<RecipesState>(RecipesState.Loading)
    val state: StateFlow<RecipesState> = _state

    private val _query = MutableStateFlow("")
    private val _isQueryEmpty = MutableStateFlow(false)
    val isQueryEmpty: StateFlow<Boolean> get() = _isQueryEmpty
    var savedRecipes: LiveData<List<RecipeRoom>> = recipesRepository.getSavedRecipes()

    //getRecipes()

    private var page = 0

    val recipes = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { RecipePagingSource(recipesRepository) }
    ).flow.cachedIn(viewModelScope)


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchRecipes: Flow<PagingData<Recipe>> = _query
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                flowOf(PagingData.empty())
            } else {
                getSearchResults(query)
            }
        }
        .cachedIn(viewModelScope)

    fun getSearchResults(query: String): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { SearchRecipePagingSource(recipesRepository, query) }
        ).flow
    }

    fun setQuery(query: String) {
        _query.value = query
    }

    /*fun getSearchResults(query: String): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { SearchRecipePagingSource(recipesRepository, query) }
        ).flow.cachedIn(viewModelScope)
    }*/


    fun getRecipes() {
        viewModelScope.launch {
            _state.value = RecipesState.Loading
            val response = recipesRepository.getAllRecipes(page, PAGE_SIZE)
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    _state.value = RecipesState.Success(body)
                }
            } else {
                _state.value = RecipesState.Error(response.message())
            }

        }
    }

    fun saveRecipe(recipe: RecipeRoom) = viewModelScope.launch {
        recipesRepository.upsertRecipe(recipe)
    }

    fun deleteRecipe(recipe: RecipeRoom) = viewModelScope.launch {
        recipesRepository.deleteRecipe(recipe)
    }

}