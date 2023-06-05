package com.danluk.whis.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.danluk.whis.R
import com.danluk.whis.json.classes.SavedTitleInfo
import com.danluk.whis.utils.shadow
import com.danluk.whis.utils.toPx
import kotlin.math.atan2
import kotlin.math.round

@Composable
fun ProgressCircle(
    savedTitleInfo: SavedTitleInfo,
    totalChapters: Int,
    outerSize: Float,
    innerSize: Float,
    modifier: Modifier = Modifier,
    onClick: ((SavedTitleInfo) ->  Unit)? = null,
    onChange: ((SavedTitleInfo) ->  Unit)? = null
) {
    val statusPainterId = when (savedTitleInfo.status) {
        "completed" -> R.drawable.book
        "in progress" -> R.drawable.book_open
        else -> R.drawable.bookmark
    }

    val center = Offset(outerSize.toPx / 2, outerSize.toPx / 2)
    val percentage = if (totalChapters > 0) {
        savedTitleInfo.finishedAmount!! / totalChapters.toFloat()
    } else {
        when (savedTitleInfo.status) {
            "completed" -> 1F
            "in progress" -> 0.5F
            else -> 0F
        }
    }

    Box(
        modifier = modifier
            .size(outerSize.dp)
            .pointerInput(savedTitleInfo.id) {
                detectTapGestures {
                    if (onClick != null) onClick(savedTitleInfo)
                }
            }
            .pointerInput(savedTitleInfo.id) {
                if (onChange != null) {
                    detectDragGestures { change, _ ->
                        val pos = Offset(
                            change.position.x,
                            outerSize.toPx - change.position.y
                        ) - center
                        val rawDeg = atan2(pos.y, pos.x) * 180 / Math.PI

                        var deg = 270 - if (rawDeg > 0) rawDeg else 360 + rawDeg
                        if (deg < 0) deg += 360

                        val titleInfo = savedTitleInfo.copy()

                        if (totalChapters > 0) {
                            titleInfo.finishedAmount = round(totalChapters * deg / 360).toInt()
                            titleInfo.status = when (titleInfo.finishedAmount) {
                                totalChapters -> "completed"
                                0 -> "planning"
                                else -> "in progress"
                            }
                        } else {
                            titleInfo.status = if (deg < 120F) {
                                "planning"
                            } else if (deg < 240F) {
                                "in progress"
                            } else {
                                "completed"
                            }
                        }

                        onChange(titleInfo)
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(Color(0x80FFFFFF), outerSize.dp, 7.dp)
                .rotate(90F)
                .background(
                    Brush.sweepGradient(
                        0F to MaterialTheme.colorScheme.secondary,
                        percentage to MaterialTheme.colorScheme.secondary,
                        percentage to MaterialTheme.colorScheme.background,
                        1F to MaterialTheme.colorScheme.background,
                    ),
                    shape = RoundedCornerShape(50)
                )
        ) {
            Box(
                modifier = Modifier
                    .size(innerSize.dp)
                    .align(Alignment.Center)
                    .background(
                        MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(50)
                    )
                    .rotate(-90F)
            ) {
                if (totalChapters > 0) {
                    ConstraintLayout(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val (topText, divider, bottomText) = createRefs()

                        Divider(
                            modifier = Modifier
                                .constrainAs(divider) {
                                    centerTo(parent)
                                }
                                .width((innerSize * 0.55F).dp),
                            color = Color.White
                        )
                        Text(
                            savedTitleInfo.finishedAmount.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = (innerSize * 0.3F).sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .constrainAs(topText) {
                                    bottom.linkTo(divider.top)
                                }
                                .fillMaxWidth()
                        )
                        Text(
                            totalChapters.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = (innerSize * 0.3F).sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .constrainAs(bottomText) {
                                    top.linkTo(divider.bottom)
                                }
                                .fillMaxWidth()
                        )
                    }
                } else {
                    Image(
                        painter = painterResource(id = statusPainterId),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize(0.4F),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}