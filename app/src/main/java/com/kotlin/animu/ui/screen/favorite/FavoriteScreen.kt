package com.kotlin.animu.ui.screen.favorite

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotlin.animu.R
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.ui.common.UIState
import com.kotlin.animu.ui.components.AnimeItem
import com.kotlin.animu.ui.components.SearchBar
import com.kotlin.animu.utils.ViewModelFactory

@Composable
fun FavoriteScreen(
    navigateToDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory.getInstance(LocalContext.current)
    )
) {
    LaunchedEffect(true) {
        viewModel.getFavorites()
    }
    var query by rememberSaveable { mutableStateOf("") }
    var enableHandler by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = enableHandler) {
        query = ""
        viewModel.getFavorites()
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
                    viewModel.getFavorites()
                    false
                } else {
                    viewModel.searchFavorite(submitQuery.trim())
                    true
                }
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        )
        viewModel.uiState.collectAsState(initial = UIState.Loading).value.let { uiState ->
            when (uiState) {
                is UIState.Loading -> Unit
                is UIState.Error -> {
                    Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                    Text(
                        text = stringResource(id = R.string.error_message, uiState.error),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("errorRoom")
                    )
                }

                is UIState.Success -> {
                    if (uiState.data.isEmpty()) {
                        Spacer(modifier = Modifier.fillMaxHeight(0.5f))
                        val message = if (enableHandler) {
                            stringResource(id = R.string.no_anime_found_favorite)
                        } else {
                            stringResource(id = R.string.empty_favorite)
                        }
                        Text(
                            text = message,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        FavoriteContent(
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
fun FavoriteContent(
    animeList: List<AnimeEntity>,
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
                imageUrl = anime.images,
                modifier = Modifier.clickable {
                    navigateToDetail(anime.malId)
                }

            )
        }
    }
}