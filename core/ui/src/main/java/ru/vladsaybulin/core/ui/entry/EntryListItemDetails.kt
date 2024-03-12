package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.EntryStatusChip
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import java.text.DecimalFormat

data class EntryListItemDetailsData(
    val kindText: String? = null,
    val year: Int? = null,
    val entryStatus: EntryStatus = EntryStatus.None,
    val volumeText: String? = null,
    val score: Float? = null
)

@Composable
fun EntryListItemDetails(
    modifier: Modifier = Modifier,
    data: EntryListItemDetailsData
) {
    Column(modifier = modifier) {
        KindAndYearText(
            kind = data.kindText,
            year = data.year?.toString()
        )
        if (data.volumeText != null) {
            Text(text = data.volumeText)
        }
        if(data.entryStatus != EntryStatus.None) {
            EntryStatusChip(
                entryStatus = data.entryStatus,
                entryType = EntryType.Anime
            )
        }
        if (data.score != null) {
            Score(score = data.score)
        }
    }
}


@Composable
private fun Score(score: Float) {
    val formatter = remember { DecimalFormat("#0.0#") }

    val contentColor = MaterialTheme.colorScheme.primary

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = ShikimoriIcons.Star,
            contentDescription = null,
            tint = contentColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatter.format(score),
            color = contentColor
        )
    }
}
