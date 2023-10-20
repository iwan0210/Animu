package com.kotlin.animu

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kotlin.animu.ui.navigation.Screen
import com.kotlin.animu.ui.screen.about.AboutScreen
import com.kotlin.animu.ui.screen.detail.DetailScreen
import com.kotlin.animu.ui.screen.favorite.FavoriteScreen
import com.kotlin.animu.ui.screen.genre.GenreScreen
import com.kotlin.animu.ui.screen.home.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimuApp(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var fabOnClick by remember { mutableStateOf({}) }
    var fabState by remember { mutableStateOf(false) }
    var fabVisible by remember { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf("") }
    Scaffold(topBar = {
        if (currentRoute == Screen.Home.route) {
            TopBar(title = title, icon = Icons.Default.Home, iconClick = { }, actionButton = {
                IconButton(onClick = { navController.navigate(Screen.Favorite.route) }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(id = R.string.bookmark)
                    )
                }
                IconButton(onClick = { navController.navigate(Screen.About.route) }) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = stringResource(
                            id = R.string.about
                        )
                    )
                }
            })
        } else {
            TopBar(
                title = title,
                icon = Icons.Default.ArrowBack,
                iconClick = { navController.navigateUp() },
                actionButton = {}
            )
        }
    }, floatingActionButton = {
        if (currentRoute == Screen.Detail.route && fabVisible) {
            ExtendedFloatingActionButton(
                onClick = fabOnClick,
                icon = {
                    Icon(
                        imageVector = if (fabState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null
                    )
                },
                text = {
                    Text(text = if (fabState) stringResource(id = R.string.remove) else stringResource(id = R.string.add))
                }
            )
        }
    }, modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                title = stringResource(id = R.string.home)
                HomeScreen(navigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                })
            }

            composable(Screen.About.route) {
                title = stringResource(id = R.string.about)
                AboutScreen()
            }

            composable(Screen.Favorite.route) {
                title = stringResource(id = R.string.favorite)
                FavoriteScreen(navigateToDetail = { animeId ->
                    navController.navigate(Screen.Detail.createRoute(animeId))
                })
            }

            composable(
                Screen.Detail.route,
                arguments = listOf(navArgument("animeId") { type = NavType.IntType })
            ) {
                title = stringResource(id = R.string.detail)
                val animeId = it.arguments?.getInt("animeId") ?: 1
                DetailScreen(
                    animeId = animeId,
                    setFabVisible = { value -> fabVisible = value },
                    setFabOnClick = { value -> fabOnClick = value },
                    setFabState = { value -> fabState = value },
                    onGenreClick = { genreId, genreName ->
                        navController.navigate(Screen.Genre.createRoute(genreId, genreName))
                    }
                )
            }

            composable(
                Screen.Genre.route,
                arguments = listOf(
                    navArgument("genreId") { type = NavType.IntType },
                    navArgument("genreName") { type = NavType.StringType }
                )
            ) {
                val genreId = it.arguments?.getInt("genreId") ?: 1
                title = it.arguments?.getString("genreName") ?: stringResource(id = R.string.genre)
                GenreScreen(
                    genreId = genreId,
                    navigateToDetail = { animeId ->
                        navController.navigate(Screen.Detail.createRoute(animeId))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    icon: ImageVector,
    iconClick: () -> Unit,
    actionButton: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = title)
        }, actions = actionButton, navigationIcon = {
            IconButton(onClick = iconClick) {
                Icon(imageVector = icon, contentDescription = stringResource(id = R.string.menu))
            }
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )


}