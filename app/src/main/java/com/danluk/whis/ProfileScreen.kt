package com.danluk.whis

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.danluk.whis.api.TitleType
import com.danluk.whis.components.ProfileInfoPanel
import com.danluk.whis.components.ProgressCircle
import com.danluk.whis.components.SavedTitlesList
import com.danluk.whis.components.TitleTypeSelector
import com.danluk.whis.json.classes.Media
import com.danluk.whis.json.classes.SavedTitleInfo
import com.danluk.whis.utils.toPx
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    innerPadding: PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel: WHISViewModel = viewModel(LocalContext.current as ComponentActivity)
    val user = viewModel.user

    if (!user.isAuthorized) {
        AuthorizationScreen()
        return
    }

    val pageCount = Int.MAX_VALUE
    val pageState = rememberPagerState(
        initialPage = pageCount / 2
    )

    var isTypeVisible by remember { mutableStateOf(true) }

    LaunchedEffect(pageState.currentPage) {
        isTypeVisible = true
    }

    val coroutineScope = rememberCoroutineScope()

    var isProgressHidden by remember { mutableStateOf(true) }
    var editTitleId by remember { mutableStateOf(0) }
    val changeProgress by animateFloatAsState(
        targetValue = if (!isProgressHidden) 1F else 0F,
        animationSpec = tween(
            125,
            if (isProgressHidden) 0 else 125,
            easing = FastOutSlowInEasing
        )
    )

    val listOfUpdates by remember { mutableStateOf(
        object {
            val edit = mutableStateListOf<SavedTitleInfo>()
            val remove = mutableStateListOf<Int>()
            var version by mutableStateOf(0)
        }
    )}

    val updatedList = remember {
        mutableStateListOf(*user.savedTitlesList.map { it.copy() }.toTypedArray())
    }

    fun updateSavedList() {
        user.editSavedList(
            context,
            arrayListOf(*listOfUpdates.edit.toTypedArray()),
            arrayListOf(*listOfUpdates.remove.toTypedArray()),
        )
    }

    LaunchedEffect(listOfUpdates.version) {
        if (listOfUpdates.edit.isNotEmpty() || listOfUpdates.remove.isNotEmpty()) {
            delay(3000)
            updateSavedList()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            if (listOfUpdates.edit.isNotEmpty() || listOfUpdates.remove.isNotEmpty()) {
                updateSavedList()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileInfoPanel(
                user,
                updatedList,
                when (pageState.currentPage % 3) {
                    1 -> TitleType.ANIME
                    2 -> TitleType.MANGA
                    else -> TitleType.ALL
                }
            )

            HorizontalPager(
                state = pageState,
                pageCount = pageCount,
                pageSpacing = 30.dp,
                modifier = Modifier
                    .fillMaxSize()
            ) { pageNum ->
                val type = when (pageNum % 3) {
                    1 -> TitleType.ANIME
                    2 -> TitleType.MANGA
                    else -> TitleType.ALL
                }

                val isUpdated = updatedList.all { title ->
                    user.savedTitlesMedia.find { it.id == title.id } != null
                }
                if (isUpdated) {
                    SavedTitlesList(
                        type,
                        updatedList,
                        user.savedTitlesMedia.toList(),
                        navController,
                        bottomPadding = innerPadding.calculateBottomPadding() + 8.dp,
                        isEditHidden = !isProgressHidden,
                        editTitleId = editTitleId,
                        onRemoved = {
                            listOfUpdates.remove.add(it.id!!)
                            listOfUpdates.version++

                            updatedList.removeIf { title -> title.id == it.id }
                        },
                        onScrolling = { isTypeVisible = !isTypeVisible }
                    ) {
                        isProgressHidden = false
                        editTitleId = it.id!!
                    }
                }
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

        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(screenWidth.dp * (1F - changeProgress), 0.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        isProgressHidden = true
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            val title = updatedList.find { it.id == editTitleId } ?: SavedTitleInfo(0, "", 0)
            val media = user.savedTitlesMedia.find { it.id == editTitleId } ?: Media(chapters = 1)

            ProgressCircle(
                title,
                media.chapters ?: media.episodes ?: 0,
                screenWidth * 0.85F,
                screenWidth * 0.65F,
                onChange = { titleInfo ->
                    val savedIndex = listOfUpdates.edit.indexOfFirst { it.id == titleInfo.id }

                    if (savedIndex != -1) {
                        listOfUpdates.edit[savedIndex] = titleInfo
                    } else {
                        listOfUpdates.edit.add(titleInfo)
                    }

                    listOfUpdates.version++

                    val i = updatedList.indexOfFirst { it.id == titleInfo.id }
                    updatedList[i] = titleInfo
                }
            )
        }
    }
}
