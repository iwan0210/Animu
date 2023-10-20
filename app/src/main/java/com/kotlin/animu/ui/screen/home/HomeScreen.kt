package com.kotlin.animu.ui.screen.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.kotlin.animu.ui.components.SearchBar
import com.kotlin.animu.utils.ViewModelFactory

@Composable
fun HomeScreen(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    var query by rememberSaveable { mutableStateOf("") }
    var enableHandler by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = enableHandler) {
        query = ""
        viewModel.getSeasonNow()
        enableHandler = false
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = query,
            onQueryChange = { newQuery ->
                query = newQuery
            },
            onSearch = { submitQuery ->
                query = submitQuery.trim()
                enableHandler = if (submitQuery.trim().isEmpty()) {
                    viewModel.getSeasonNow()
                    false
                } else {
                    viewModel.searchAnime(submitQuery.trim())
                    true
                }

            },
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        )
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
                        HomeContent(
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
fun HomeContent(
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