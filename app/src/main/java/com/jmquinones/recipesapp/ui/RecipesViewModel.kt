package com.jmquinones.recipesapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.jmquinones.recipesapp.data.paging.RecipePagingSource
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.models.ResponseWrapper
import com.jmquinones.recipesapp.data.repository.RecipesRepository
import com.jmquinones.recipesapp.utils.Constants.Companion.PAGE_SIZE
import com.jmquinones.recipesapp.utils.RecipesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipesViewModel(val recipesRepository: RecipesRepository, app:Application): ViewModel(){
    private var _state = MutableStateFlow<RecipesState>(RecipesState.Loading)
    val state: StateFlow<RecipesState> = _state
    private var page = 0

    /*init {
        getRecipes()
    }*/

    val recipes = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { RecipePagingSource(recipesRepository) }
    ).flow.cachedIn(viewModelScope)

    fun getRecipes(){
        viewModelScope.launch {
            _state.value = RecipesState.Loading
            val response = recipesRepository.getAllRecipes(page, PAGE_SIZE)
            if (response.isSuccessful){
                response.body()?.let { body ->
                    _state.value = RecipesState.Success(body)
                }
            } else {
                _state.value = RecipesState.Error(response.message())
            }

        }
    }

}