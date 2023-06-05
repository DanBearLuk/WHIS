package com.danluk.whis.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp

@Composable
fun AuthButton(
    text: String,
    isLeft: Boolean,
    callback: () -> Unit
) {
    val shape = if (isLeft) GenericShape { size, _ ->
        val w = size.width
        val h = size.height

        addArc(Rect(Offset(0.5F * h, 0.5F * h), h / 2), 90F, 180F)
        moveTo(0.5F * h, h)
        lineTo(0.95F * w, h)
        lineTo(w, 0F)
        lineTo(0.5F * h, 0F)
    } else GenericShape { size, _ ->
        val w = size.width
        val h = size.height

        addArc(Rect(Offset(w - 0.5F * h, 0.5F * h), h / 2), 90F, -180F)
        moveTo(w - 0.5F * h, h)
        lineTo(0F, h)
        lineTo(0.05F * w, 0F)
        lineTo(w - 0.5F * h, 0F)
    }


    Box(
        modifier = Modifier
            .fillMaxWidth(if (isLeft) 0.5F else 1F)
    ) {
        Button(
            onClick = {
                callback()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =
                if (isLeft) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            shape = shape,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}