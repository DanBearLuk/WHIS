package com.danluk.whis

import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.danluk.whis.api.getTitleInfo
import com.danluk.whis.classes.Direction
import com.danluk.whis.components.DescriptionInnerPage
import com.danluk.whis.components.GeneralInfoInnerPage
import com.danluk.whis.components.SaveTitleButton
import com.danluk.whis.components.TagsInnerPage
import com.danluk.whis.components.pagePointer
import com.danluk.whis.json.classes.Media
import com.danluk.whis.json.classes.SavedTitleInfo
import com.danluk.whis.json.classes.TitleInfo

@ExperimentalFoundationApi
@ExperimentalLayoutApi
@Composable
fun TitleInfoScreen(
    innerPadding: PaddingValues,
    titleId: Int
) {
    val context = LocalContext.current
    val viewModel: WHISViewModel = viewModel(LocalContext.current as ComponentActivity)
    val user = viewModel.user

    var titleInfo by remember { mutableStateOf<TitleInfo?>(null) }

    LaunchedEffect(titleId) {
        getTitleInfo(titleId, context = context) {
            titleInfo = it
        }
    }

    val pageCount = Int.MAX_VALUE
    val pageState = rememberPagerState(
        initialPage = pageCount / 2
    )

    Column(
        modifier = Modifier
            .padding(start = 8.dp, top = 16.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            pagePointer(
                pageState = pageState,
                direction = Direction.LEFT
            )

            Box(
                modifier = Modifier
                    .weight(2F)
                    .aspectRatio(0.714F, true)
                    .clip(RoundedCornerShape(15.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(titleInfo?.Media?.coverImage?.large),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                if (user.isAuthorized) {
                    SaveTitleButton(user.savedTitlesList.any { it.id == titleId }) {
                        viewModel.user.editSavedList(
                            context,
                            edit = arrayListOf(
                                SavedTitleInfo(titleId, "planning", 0)
                            )
                        )
                    }
                }
            }

            pagePointer(
                pageState = pageState,
                direction = Direction.RIGHT
            )
        }

        val title = titleInfo?.Media?.title
        Text(
            title?.english ?: title?.romaji ?: "",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(bottom = 10.dp, start = 18.dp, end = 18.dp)
        )

        HorizontalPager(
            state = pageState,
            pageCount = pageCount,
            pageSpacing = 30.dp,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
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
        ) { pageNum ->
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 15.dp)
            ) {
                when (pageNum % 3) {
                    0 -> DescriptionInnerPage(titleInfo?.Media?.description ?: "")
                    1 -> GeneralInfoInnerPage(titleInfo?.Media ?: Media())
                    2 -> TagsInnerPage(titleInfo?.Media?.tags ?: ArrayList(0))
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(innerPadding.calculateBottomPadding() + 24.dp)
                )
            }
        }
    }
}
