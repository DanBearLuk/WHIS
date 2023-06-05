package com.danluk.whis.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DescriptionInnerPage(text: String) {
    Text(
        text
            .replace("<br>", "\n")
            .replace(Regex("<\\/?.+?>"), ""),
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Justify,
        modifier = Modifier
            .padding(start = 18.dp, end = 18.dp)
    )
}