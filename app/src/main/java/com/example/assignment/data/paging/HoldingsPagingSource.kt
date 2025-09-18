package com.example.assignment.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.assignment.domain.model.Holding

class HoldingsPagingSource(
    private val fetchHoldings: suspend (page: Int, pageSize: Int) -> List<Holding>
) : PagingSource<Int, Holding>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Holding> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val holdings = fetchHoldings(page, pageSize)

            LoadResult.Page(
                data = holdings,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (holdings.size < pageSize) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Holding>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}



