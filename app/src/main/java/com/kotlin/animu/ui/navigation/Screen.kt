package com.kotlin.animu.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object About : Screen("about")
    data object Favorite : Screen("favorite")
    data object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int) = "detail/$animeId"
    }
    data object Genre : Screen("genre/{genreId}/{genreName}") {
        fun createRoute(genreId: Int, genreName: String) = "genre/$genreId/$genreName"
    }
}
