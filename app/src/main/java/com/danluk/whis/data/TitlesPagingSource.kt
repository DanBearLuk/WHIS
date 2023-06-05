package com.danluk.whis.data

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.danluk.whis.api.TitleType
import com.danluk.whis.api.getTitles
import com.danluk.whis.json.classes.Media
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TitlesPagingSource(
    val context: Context,
    val type: TitleType? = null,
    val name: String? = null,
    val ids: Array<Int>? = null
) : PagingSource<Int, Media>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Media> {
        return suspendCoroutine { continuation ->
            val nextPageNumber = params.key ?: 1

            getTitles(
                nextPageNumber,
                params.loadSize,
                context,
                type,
                name,
                ids
            ) {
                val currentPage = it.pageInfo?.currentPage!!
                val hasNextPage = it.pageInfo?.hasNextPage!!

                continuation.resume(
                    LoadResult.Page(
                        data = it.media,
                        prevKey = if (currentPage > 1) currentPage - 1 else null,
                        nextKey = if (hasNextPage) currentPage + 1 else null
                    )
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
