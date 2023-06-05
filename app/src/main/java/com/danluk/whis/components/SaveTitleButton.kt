package com.danluk.whis.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.danluk.whis.R
import com.danluk.whis.utils.shadow

@Composable
fun BoxScope.SaveTitleButton(
    isSaved: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(50)

    Box(
        modifier = Modifier
            .padding(6.dp)
            .height(52.dp)
            .width(52.dp)
            .shadow(Color(0xC0FFFFFF), 52.dp, 7.dp)
            .background(
                if (isSaved) Color(0xE076D848)
                else Color(0xE0FF5252),
                shape = shape
            )
            .align(Alignment.BottomEnd)
    ) {
        val painterId =
            if (isSaved) R.drawable.checkmark else R.drawable.plus

        Image(
            painter = painterResource(id = painterId),
            contentDescription = null,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        if (!isSaved) onClick()
                    }
                ),
            contentScale = ContentScale.Fit
        )
    }
}