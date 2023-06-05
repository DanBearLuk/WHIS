package com.danluk.whis.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.danluk.whis.R
import com.danluk.whis.classes.Direction
import com.danluk.whis.utils.mod
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun RowScope.pagePointer(
    pageState: PagerState,
    direction: Direction
) {
    val coroutineScope = rememberCoroutineScope()

    val labels = listOf("Desc", "Info", "Tags")

    val offset = when (direction) {
        Direction.LEFT -> -1
        else -> 1
    }
    val idx = mod(pageState.currentPage + offset, 3)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .weight(1F)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    coroutineScope.launch {
                        pageState.animateScrollToPage(
                            pageState.currentPage + offset,
                            animationSpec = tween(250)
                        )
                    }
                }
            )
    ) {
        Text(
            labels[idx],
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.labelLarge
        )

        Image(
            painterResource(id = R.drawable.arrow),
            contentDescription = labels[idx],
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 5.dp)
                .height(26.dp)
                .width(26.dp)
                .rotate(if (direction == Direction.LEFT) 90F else -90F)
        )
    }
}