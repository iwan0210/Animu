package com.kotlin.animu.ui.screen.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kotlin.animu.R
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.data.model.Studio
import com.kotlin.animu.ui.common.UIState
import com.kotlin.animu.ui.components.LoadingContent
import com.kotlin.animu.ui.utils.Size
import com.kotlin.animu.utils.ViewModelFactory

@Composable
fun DetailScreen(
    animeId: Int,
    setFabVisible: (Boolean) -> Unit,
    setFabState: (Boolean) -> Unit,
    setFabOnClick: (() -> Unit) -> Unit,
    onGenreClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {

    LaunchedEffect(true) {
        viewModel.getAnimeById(animeId)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.uiState
            .collectAsState(initial = UIState.Loading).value.let { uiState ->
                when (uiState) {
                    is UIState.Loading -> {
                        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                        LoadingContent()
                    }

                    is UIState.Error -> {
                        setFabVisible(false)
                        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                        Text(
                            text = stringResource(id = R.string.error_message, uiState.error),
                            textAlign = TextAlign.Center
                        )
                    }

                    is UIState.Success -> {
                        setFabVisible(true)
                        LaunchedEffect(true) {
                            viewModel.checkFavorite(uiState.data.malId)
                        }
                        viewModel.isChecked
                            .collectAsState(initial = false).value.let { value ->
                                LaunchedEffect(value) {
                                    setFabState(value)
                                    setFabOnClick {
                                        uiState.data.let {
                                            val anime = AnimeEntity(
                                                malId = it.malId,
                                                images = it.images.jpg.largeImageUrl,
                                                title = it.title,
                                                titleJapanese = it.titleJapanese,
                                                status = it.status,
                                                score = it.score,
                                                season = it.season,
                                                year = it.year
                                            )
                                            if (!value) {
                                                viewModel.insert(anime)
                                            } else {
                                                viewModel.delete(anime)
                                            }
                                        }
                                    }
                                }
                            }
                        uiState.data.let {
                            DetailContent(
                                imageUrl = it.images.jpg.largeImageUrl,
                                type = it.type,
                                season = it.season,
                                year = it.year,
                                status = it.status,
                                episode = it.episodes,
                                duration = it.duration,
                                score = it.score,
                                genres = it.genres,
                                title = it.title,
                                titleJapanese = it.titleJapanese,
                                synopsis = it.synopsis,
                                source = it.source,
                                aired = it.aired.string,
                                studio = if (it.studios.isNotEmpty()) it.studios[0].name else stringResource(
                                    id = R.string.unknown
                                ),
                                onGenreClick = onGenreClick
                            )
                        }
                    }

                }
            }
    }

}


@OptIn(ExperimentalLayoutApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun DetailContent(
    imageUrl: String,
    type: String,
    season: String?,
    year: Int?,
    status: String,
    episode: Int,
    duration: String,
    score: Double?,
    genres: List<Studio>,
    title: String,
    titleJapanese: String,
    synopsis: String,
    source: String,
    aired: String,
    studio: String,
    onGenreClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        GlideImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.width((Size.width() / 1.7).dp),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (season != null) {
                Text(text = "$type, $season $year")
            } else {
                Text(text = type)
            }
            Text(text = status)
            score?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null)
                    Text(text = " $score")
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.episodes, episode))
            Text(text = duration)
        }
        Spacer(modifier = Modifier.height(20.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            genres.forEach {
                OutlinedButton(onClick = { onGenreClick(it.malId, it.name) }) {
                    Text(text = it.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Text(
                    text = "( $titleJapanese )",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = synopsis,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.source))
                        Text(text = source)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.aired))
                        Text(text = aired)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(id = R.string.studio))
                        Text(text = studio)
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}