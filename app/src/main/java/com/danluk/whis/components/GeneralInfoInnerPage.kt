package com.danluk.whis.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danluk.whis.json.classes.Media
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun GeneralInfoInnerPage(media: Media) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
        userScrollEnabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        val dateFormat = DateTimeFormatter.ofPattern("MMM d, YYYY", Locale.ENGLISH)

        val labels =
            arrayOf<String?>("Format", "Season", "Source", "Popularity", "Status", "Start Date")
        val values = arrayOf(
            media.format?.capitalize(),
            (media.season?.capitalize() ?: "") + " " + media.startDate?.year,
            media.source?.capitalize(),
            media.popularity?.toString(),
            media.status?.capitalize(),
            if (media.startDate != null)
                LocalDate.of(
                    media.startDate?.year ?: 0,
                    media.startDate?.month ?: 0,
                    media.startDate?.day ?: 0,
                ).format(dateFormat)
            else null
        )
        values.forEachIndexed { i, value ->
            if (value == null) labels[i] = null
        }

        val labelsOfNotNull = listOfNotNull(*labels)
        val valuesOfNotNull = listOfNotNull(*values)

        items(labelsOfNotNull.size) { i ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(110.dp)
            ) {
                Text(
                    labelsOfNotNull[i],
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                )
                Text(
                    valuesOfNotNull[i],
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}