package com.danluk.whis.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.danluk.whis.json.classes.Media
import com.danluk.whis.utils.navigateTo

@Composable
fun SearchResult(
    navController: NavController,
    media: Media
) {
    val title = media.title

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(175.dp)
            .padding(0.dp, 15.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    navigateTo(
                        navController,
                        "TitleInfo/${media.id}",
                        false
                    )
                }
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(0.714F, true)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Image(
                painter = rememberAsyncImagePainter(media.coverImage?.large),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp)
        ) {
            Text(
                title?.english ?: title?.romaji ?: "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                (media.description ?: "")
                    .replace(Regex("<\\/?.+?>"), ""),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}