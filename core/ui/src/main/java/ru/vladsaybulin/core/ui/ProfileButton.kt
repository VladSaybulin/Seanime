package ru.vladsaybulin.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.user.UserImage

@Composable
fun ProfileButton(
    image: UserImage?,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        val userImagePainter = image?.x64Url?.let { rememberAsyncImagePainter(it) }
            ?: rememberVectorPainter(SeanimeIcons.AccountCircle)
        Image(
            painter = userImagePainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .let { if (image != null) it.size(32.dp) else it }
                .clip(CircleShape),
            colorFilter = if (image == null) ColorFilter.tint(SeanimeTheme.colorScheme.onSurface) else null
        )
    }
}