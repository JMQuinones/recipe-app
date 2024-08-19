package com.jmquinones.recipesapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jmquinones.recipesapp.data.repository.RecipesRepository
import com.jmquinones.recipesapp.models.Recipe
import com.jmquinones.recipesapp.utils.NetworkUtils
import java.io.IOException

class SearchRecipePagingSource(
    private val recipesRepository: RecipesRepository,
    private val query: String
) : PagingSource<Int, Recipe>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val page = params.key ?: 0
        return try {
            if (!NetworkUtils.hasInternetConnection()){
                throw IOException("Revise su conexion a Internet")
            }
            val response = recipesRepository.searchByTitleOrAuthor(page, params.loadSize, query)
            if (!response.isSuccessful) {
                throw Exception(response.message())
            }

            Log.d("PagingSource", "Loaded page $page")
            LoadResult.Page(
                data = response.body()!!.recipes,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page >= response.body()!!.totalPages) null else page + 1
            )

        } catch (e: Exception) {
            Log.e("PAGING_SOURCE", e.message!!)
            LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}