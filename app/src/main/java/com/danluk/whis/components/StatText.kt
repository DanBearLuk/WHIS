package com.danluk.whis.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatText(
    label: String,
    count: Int
) {
    Row {
        Text(
            label,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            modifier = Modifier.fillMaxWidth(0.75F)
        )
        Text(
            count.toString(),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(start = 20.dp)
                .fillMaxWidth()
        )
    }
}