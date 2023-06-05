package com.danluk.whis.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danluk.whis.R
import com.danluk.whis.api.TitleType
import com.danluk.whis.api.deauthorize
import com.danluk.whis.classes.User
import com.danluk.whis.json.classes.SavedTitleInfo

@Composable
fun ProfileInfoPanel(
    user: User,
    updatedList: SnapshotStateList<SavedTitleInfo>,
    chosenTitleType: TitleType
) {
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp, 12.dp, 8.dp, 15.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                user.username!!,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.displayMedium,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(end = 30.dp)
            )

            Button(
                onClick = {
                    user.deauth(context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(35.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.log_out),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Column(
            modifier = Modifier
                .widthIn(max = 225.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(25.dp, 15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var filteredSavedList = updatedList.toList()
            if (chosenTitleType != TitleType.ALL) {
                filteredSavedList = filteredSavedList.filter { title ->
                    user.savedTitlesMedia.find { it.id == title.id }?.type == chosenTitleType.name
                }
            }

            val inProgress = filteredSavedList.count { it.status == "in progress" }
            val completed = filteredSavedList.count { it.status == "completed" }
            val planning = filteredSavedList.count { it.status == "planning" }

            StatText("Completed", completed)
            StatText("In Progress", inProgress)
            StatText("Planning", planning)
        }
    }
}