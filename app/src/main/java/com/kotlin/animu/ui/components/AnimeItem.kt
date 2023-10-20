package com.kotlin.animu.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kotlin.animu.R
import com.kotlin.animu.ui.utils.Size

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AnimeItem(
    title: String,
    altTitle: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    score: Double? = null,
    status: String? = null,
    season: String? = null,
    year: Int? = null,
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row {
            GlideImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .width((Size.width() / 3.5).dp)
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = altTitle,
                    fontSize = 12.sp,
                    lineHeight = 14.sp,
                    fontStyle = FontStyle.Italic,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = score.toString())
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Text(
                        text = stringResource(
                            id = R.string.item_status,
                            status ?: stringResource(id = R.string.unknown)
                        ),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (season != null) {
                        Text(
                            text = "$season $year",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}