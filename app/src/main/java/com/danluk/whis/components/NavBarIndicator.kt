package com.danluk.whis.components

import android.animation.ArgbEvaluator
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NavBarIndicator(
    xOffset: Dp,
    yOffset: Dp,
    transformProgression: Float
) {
    Box(
        modifier = Modifier
            .offset(xOffset, yOffset)
            .height(46.dp)
            .width(46.dp)
            .border(
                2.dp,
                Color(
                    ArgbEvaluator().evaluate(
                        transformProgression,
                        Color.White.toArgb(),
                        MaterialTheme.colorScheme.secondary.toArgb()
                    ) as Int
                ),
                shape = CircleShape
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(transformProgression)
                .background(
                    MaterialTheme.colorScheme.secondary,
                    shape = CircleShape
                )
                .align(Alignment.Center)
        )
    }
}