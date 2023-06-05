package com.danluk.whis.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.danluk.whis.json.classes.Tag

@ExperimentalLayoutApi
@Composable
fun TagsInnerPage(tags: ArrayList<Tag>) {
    val colorScheme = MaterialTheme.colorScheme

    val content = @Composable {
        tags.forEach { tag ->
            Text(
                tag.name ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(6.dp)
                    .background(
                        if ((tag.rank ?: 0) >= 80) colorScheme.secondary else colorScheme.surface,
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(12.dp, 8.dp)
            )
        }
    }

    SubcomposeLayout(
        modifier = Modifier
            .fillMaxWidth()
    ) { constraints ->
        val looseConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )
        var placeables = subcompose(1, content).map {
            it.measure(looseConstraints)
        }

        val sizes = placeables.map {
            mutableListOf(
                it.width,
                it.height,
            )
        }
        val positions = List(sizes.size) { mutableListOf(0, 0) }
        val maxWidth = constraints.maxWidth

        var sumWidth = 0
        var sumCount = 0
        var curY = 0
        for (i in positions.indices.plus(positions.size)) {
            if (i == positions.size || sumWidth + sizes[i][0] > maxWidth) {
                var maxHeight = 0
                var curX = 0

                sizes.subList(i - sumCount, i).forEachIndexed { index, size ->
                    size[0] = (maxWidth.toFloat() / sumWidth * size[0]).toInt()

                    positions[i - sumCount + index][0] = curX
                    positions[i - sumCount + index][1] = curY
                    curX += size[0]

                    if (maxHeight < size[1]) maxHeight = size[1]
                }

                curY += maxHeight
                if (i != positions.size) {
                    sumWidth = sizes[i][0]
                    sumCount = 1
                }
            } else {
                sumWidth += sizes[i][0]
                sumCount++
            }
        }

        placeables = subcompose(2, content).mapIndexed { index, measurable ->
            measurable.measure(Constraints.fixedWidth(sizes[index][0]))
        }

        layout(constraints.maxWidth, curY) {
            placeables.forEachIndexed { i, placeable ->
                placeable.place(positions[i][0], positions[i][1])
            }
        }
    }
}