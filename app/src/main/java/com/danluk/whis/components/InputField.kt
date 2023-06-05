package com.danluk.whis.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin

@Composable
fun InputField(
    label: String,
    controlVariable: MutableState<String>,
    modifier: Modifier = Modifier
): () -> Unit {
    var errorTargetValue by remember { mutableStateOf(0F) }

    val errorProgress by animateFloatAsState(
        targetValue = errorTargetValue,
        animationSpec = tween(400, easing = LinearEasing)
    )
    val errorOffset = sin(Math.PI * errorProgress) * 10

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            label,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .padding(12.dp, 0.dp)
        )

        BasicTextField(
            value = controlVariable.value,
            textStyle = MaterialTheme.typography.displayMedium,
            onValueChange = { newValue: String ->
                controlVariable.value = newValue
            },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .height(50.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(0.dp, 50.dp, 50.dp, 50.dp)
                        )
                        .padding(32.dp, 0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    innerTextField()
                }
            },
            modifier = Modifier
                .offset(errorOffset.dp, 0.dp)
                .fillMaxWidth()
        )
    }

    return { errorTargetValue = if (errorTargetValue == 0F) 5F else 0F }
}