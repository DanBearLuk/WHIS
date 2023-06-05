package com.danluk.whis.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.danluk.whis.api.TitleType
import com.danluk.whis.json.classes.Media
import com.danluk.whis.json.classes.SavedTitleInfo
import com.danluk.whis.utils.navigateTo

@Composable
fun SavedTitlesList(
    type: TitleType,
    savedList: List<SavedTitleInfo>,
    mediaList: List<Media>,
    navController: NavController,
    bottomPadding: Dp,
    onScrolling: () -> Unit,
    isEditHidden: Boolean,
    editTitleId: Int,
    onRemoved: (SavedTitleInfo) -> Unit,
    showChangeScreen: (SavedTitleInfo) -> Unit
) {
    var filteredSavedList = savedList.toList()
    if (type != TitleType.ALL) {
        filteredSavedList = filteredSavedList.filter { title ->
            mediaList.find { it.id == title.id }?.type == type.name
        }
    }

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(lazyGridState.isScrollInProgress) {
        if (lazyGridState.isScrollInProgress) onScrolling()
    }

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, bottomPadding),
        modifier = Modifier
            .fillMaxSize()
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
            filteredSavedList.size,
            key = { index -> filteredSavedList[index].id!! }
        ) { index ->
            val media = mediaList.find { it.id == filteredSavedList[index].id }!!
            val titleInfo = filteredSavedList[index]

            SavedTitle(
                titleInfo,
                media,
                isEditing = isEditHidden && titleInfo.id == editTitleId,
                onClick = {
                    navigateTo(
                        navController,
                        "TitleInfo/${it.id}",
                        false
                    )
                },
                onProgressClick = showChangeScreen,
                onRemoved = onRemoved
            )
        }
    }
}