package com.danluk.whis

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.danluk.whis.components.InputField
import com.danluk.whis.components.LoadingIcon
import com.danluk.whis.components.SearchResult
import com.danluk.whis.data.TitlesPagingSource
import com.danluk.whis.json.classes.Media
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@ExperimentalMaterial3Api
@Composable
fun SearchScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController
) {
    val viewModel: WHISViewModel = viewModel(LocalContext.current as ComponentActivity)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val searchPrompt = rememberSaveable { mutableStateOf("") }

    LaunchedEffect(searchPrompt.value) {
        if (searchPrompt.value.isBlank()) {
            viewModel.searchResults.value = null
            viewModel.searchPrompt = ""
            return@LaunchedEffect
        }
        if (viewModel.searchPrompt == searchPrompt.value) {
            return@LaunchedEffect
        }

        delay(200)

        viewModel.searchResults.value = Pager(
            PagingConfig(pageSize = 30)
        ) {
            TitlesPagingSource(
                context.applicationContext,
                name = searchPrompt.value
            )
        }.flow.cachedIn(coroutineScope)
        viewModel.searchPrompt = searchPrompt.value
    }

    val lazyColumnState = rememberLazyListState()
    val results = viewModel.searchResults.value?.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        InputField(
            "Search",
            searchPrompt,
            Modifier.padding(17.dp, 30.dp, 17.dp, 15.dp)
        )

        if (results != null && results.itemCount == 0) {
            LoadingIcon(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5F)
            )
        } else if (results != null) {
            val bottomPadding = innerPadding.calculateBottomPadding()

            LazyColumn(
                state = lazyColumnState,
                contentPadding = PaddingValues(8.dp, 15.dp, 8.dp, bottomPadding),
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                    .drawWithContent {
                        val colors = listOf(
                            Color.Transparent,
                            Color.Black
                        )
                        drawContent()
                        drawRect(
                            brush = Brush.verticalGradient(colors, endY = 30.dp.value),
                            blendMode = BlendMode.DstIn
                        )
                    }
            ) {
                items(
                    results.itemCount,
                    key = { results[it]?.id!! }
                ) {
                    SearchResult(navController, results.get(it)!!)
                }
            }
        }
    }
}
