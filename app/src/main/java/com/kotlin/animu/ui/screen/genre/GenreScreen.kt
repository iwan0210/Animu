package com.kotlin.animu.ui.screen.genre

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlin.animu.R
import com.kotlin.animu.data.model.AnimeItem
import com.kotlin.animu.ui.common.UIState
import com.kotlin.animu.ui.components.AnimeItem
import com.kotlin.animu.ui.components.LoadingContent
import com.kotlin.animu.utils.ViewModelFactory

@Composable
fun GenreScreen(
    genreId: Int,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GenreViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    LaunchedEffect(true) {
        viewModel.genreAnime(genreId)
    }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.uiState.collectAsState(initial = UIState.Loading).value.let { uiState ->
            when (uiState) {
                is UIState.Loading -> {
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                    LoadingContent()
                }

                is UIState.Error -> {
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                    Text(
                        text = stringResource(id = R.string.error_message, uiState.error),
                        textAlign = TextAlign.Center
                    )
                }

                is UIState.Success -> {
                    if (uiState.data.isEmpty()) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                        Text(
                            text = stringResource(id = R.string.no_anime_found),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        GenreContent(
                            animeList = uiState.data,
                            navigateToDetail = navigateToDetail
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun GenreContent(
    animeList: List<AnimeItem>,
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(animeList, key = { it.malId }) { anime ->
            AnimeItem(
                title = anime.title,
                altTitle = anime.titleJapanese,
                score = anime.score,
                status = anime.status,
                season = anime.season,
                year = anime.year,
                imageUrl = anime.images.jpg.largeImageUrl,
                modifier = Modifier.clickable {
                    navigateToDetail(anime.malId)
                }
            )
        }
    }
}