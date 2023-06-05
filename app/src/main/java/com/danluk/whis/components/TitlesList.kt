package com.danluk.whis.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.danluk.whis.WHISViewModel
import com.danluk.whis.api.TitleType
import com.danluk.whis.utils.navigateTo

@Composable
fun TitlesList(
    type: TitleType,
    navController: NavController,
    onScrolling: () -> Unit
) {
    val viewModel: WHISViewModel = viewModel(LocalContext.current as ComponentActivity)

    val lazyGridState = viewModel.titlesGridState[type]!!

    LaunchedEffect(lazyGridState.isScrollInProgress) {
        if (lazyGridState.isScrollInProgress) onScrolling()
    }

    val titlesList = viewModel.titlesMap[type]!!.collectAsLazyPagingItems()

    if (titlesList.itemCount == 0) return

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(titlesList.itemCount, key = { titlesList[it]?.id!! }) { index ->
            val media = titlesList[index]!!
            val url = media.coverImage?.large
            val title = media.title

            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .aspectRatio(0.714F, true)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            navigateTo(
                                navController,
                                "TitleInfo/${media.id}",
                                false
                            )
                        }
                    )
            ) {
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                0F to Color(0x00000000),
                                1F to Color(0xBF000000)
                            )
                        )
                ) {
                    Text(
                        title?.english ?: title?.romaji ?: "",
                        modifier = Modifier
                            .padding(10.dp, 5.dp)
                            .align(Alignment.BottomStart),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
