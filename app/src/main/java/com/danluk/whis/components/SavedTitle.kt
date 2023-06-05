package com.danluk.whis.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.danluk.whis.R
import com.danluk.whis.json.classes.Media
import com.danluk.whis.json.classes.SavedTitleInfo
import kotlinx.coroutines.delay

@Composable
fun SavedTitle(
    titleInfo: SavedTitleInfo,
    media: Media,
    isEditing: Boolean,
    onClick: (SavedTitleInfo) -> Unit,
    onProgressClick: (SavedTitleInfo) -> Unit,
    onRemoved: (SavedTitleInfo) -> Unit
) {
    val url = media.coverImage?.large
    val title = media.title
    val totalChapters = media.chapters ?: media.episodes ?: 0

    var isRemoving by remember { mutableStateOf(false) }
    val removeStateProgress by animateFloatAsState(
        targetValue = if (isRemoving) 1F else 0F,
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    )
    LaunchedEffect(isRemoving) {
        if (isRemoving) {
            delay(3000)
            isRemoving = false
        }
    }

    val changeProgress by animateFloatAsState(
        targetValue = if (isEditing) 1F else 0F,
        animationSpec = tween(
            125,
            if (isEditing) 0 else 125,
            easing = FastOutSlowInEasing
        )
    )

    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .aspectRatio(0.714F, true)
            .clip(shape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        isRemoving = true
                    }
                ) {
                    onClick(titleInfo)
                }
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
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

        val offsetX = -(65.dp * changeProgress)

        ProgressCircle(
            titleInfo,
            totalChapters,
            52F,
            40F,
            modifier = Modifier
                .zIndex(if (isRemoving || removeStateProgress > 0F) 0F else 1F)
                .padding(6.dp)
                .offset(offsetX, 0.dp)
                .align(Alignment.TopStart),
            onClick = onProgressClick
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = removeStateProgress }
                .background(
                    Color(0xCCFF5252),
                    shape = shape
                )
                .border(
                    BorderStroke(2.dp, Color(0xFFFF5252)),
                    shape = shape
                )
                .pointerInput(isRemoving) {
                    if (isRemoving) {
                        detectTapGestures {
                            onRemoved(titleInfo)
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painterResource(id = R.drawable.trashcan),
                contentDescription = "remove icon",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(26.dp)
                    .width(26.dp)
            )
        }
    }
}