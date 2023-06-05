package com.danluk.whis.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.danluk.whis.utils.mod
import com.danluk.whis.utils.shadow
import com.danluk.whis.utils.toDp
import com.danluk.whis.utils.toPx

@Composable
fun TitleTypeSelector(
    isVisible: Boolean,
    bottomPadding: Float,
    currentPageOffset: Float,
    currentPage: Int,
    onClick: () -> Unit
) {
    val hideAnimProgress by animateFloatAsState(
        targetValue = if (isVisible) 1F else 0F,
        animationSpec = tween(250, easing = FastOutSlowInEasing)
    )

    Layout(
        content = {
            val shape = RoundedCornerShape(100.dp, 100.dp, 12.dp, 12.dp)
            val size = Size(108.toPx, 60.toPx)

            Box(
                modifier = Modifier
                    .width(size.width.toDp)
                    .height(size.height.toDp)
                    .graphicsLayer { alpha = hideAnimProgress }
                    .shadow(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        outline = shape.createOutline(
                            size,
                            LayoutDirection.Ltr,
                            density = Density(LocalContext.current)
                        ),
                        blurRadius = 8.dp
                    )
                    .background(
                        MaterialTheme.colorScheme.surface,
                        shape = shape
                    )
                    .clip(shape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onClick() }
                    )
            ) {
                var currentIndex = currentPage + currentPageOffset

                if (currentIndex < -0.5) currentIndex += 3F
                else if (currentIndex > 2.5) currentIndex -= 3F

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .offset(0.dp, 30.dp)
                        .rotate(currentIndex * 180F)
                ) {
                    val titleTypes = listOf("All", "Anime", "Manga")

                    var index = when {
                        currentPageOffset < 0 -> mod(currentPage - 1, 3)
                        else -> mod(currentPage + 1, 3)
                    }

                    val isEven = currentPage % 2 == 0
                    val firstText = titleTypes[if (isEven) currentPage else index]
                    val secondText = titleTypes[if (!isEven) currentPage else index]

                    Text(
                        text = firstText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(0.dp, -(27.dp))
                    )
                    Text(
                        text = secondText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(0.dp, 27.dp)
                            .rotate(180F)
                    )
                }
            }
        }
    ) { measurables, constraints ->
        val box = measurables[0].measure(constraints)

        val bottomPadding = constraints.maxHeight - bottomPadding

        val x = ((constraints.maxWidth - box.width) / 2)
        val y = (bottomPadding - hideAnimProgress * (box.height + 3.toPx)).toInt()

        layout(constraints.maxWidth, constraints.maxHeight) {
            box.place(x, y)
        }
    }
}