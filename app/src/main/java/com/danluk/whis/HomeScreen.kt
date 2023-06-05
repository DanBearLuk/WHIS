package com.danluk.whis

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.danluk.whis.api.TitleType
import com.danluk.whis.components.TitleTypeSelector
import com.danluk.whis.components.TitlesList
import com.danluk.whis.utils.toPx
import kotlinx.coroutines.launch

@Composable
@ExperimentalFoundationApi
fun HomeScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController
) {
    val pageCount = Int.MAX_VALUE
    val pageState = rememberPagerState(
        initialPage = pageCount / 2
    )

    var isTypeVisible by remember { mutableStateOf(true) }
    LaunchedEffect(pageState.currentPage) {
        isTypeVisible = true
    }

    val coroutineScope = rememberCoroutineScope()

    Box {
        HorizontalPager(
            state = pageState,
            pageCount = pageCount,
            pageSpacing = 30.dp
        ) { pageNum ->
            when (pageNum % 3) {
                0 -> TitlesList(TitleType.ALL, navController) { isTypeVisible = !isTypeVisible }
                1 -> TitlesList(TitleType.ANIME, navController) { isTypeVisible = !isTypeVisible }
                2 -> TitlesList(TitleType.MANGA, navController) { isTypeVisible = !isTypeVisible }
            }
        }

        TitleTypeSelector(
            isTypeVisible,
            innerPadding.calculateBottomPadding().value.toPx,
            pageState.currentPageOffsetFraction,
            pageState.currentPage % 3
        ) {
            coroutineScope.launch {
                pageState.animateScrollToPage(
                    pageState.currentPage + 1,
                    animationSpec = tween(250)
                )
            }
        }
    }
}
