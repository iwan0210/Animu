package com.kotlin.animu.ui.screen.favorite

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kotlin.animu.R
import com.kotlin.animu.data.AnimeRepository
import com.kotlin.animu.data.local.AnimeDao
import com.kotlin.animu.data.local.AnimeDatabase
import com.kotlin.animu.data.model.AnimeEntity
import com.kotlin.animu.data.remote.ApiConfig
import com.kotlin.animu.ui.theme.AnimuTheme
import com.kotlin.animu.utils.onNodeWithStringId
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteScreenTest {

    private val apiService = ApiConfig.getApiService()
    private lateinit var database: AnimeDatabase
    private lateinit var animeDao: AnimeDao
    private lateinit var context: Context
    private lateinit var repository: AnimeRepository

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        context = composeTestRule.activity
        database = Room.inMemoryDatabaseBuilder(context, AnimeDatabase::class.java).build()
        animeDao = database.animeDao()
        repository = AnimeRepository(apiService, animeDao)
    }

    @Test
    fun favoriteScreenEmpty() {
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    FavoriteScreen(
                        navigateToDetail = {}
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithStringId(R.string.empty_favorite).assertExists()
    }

    @Test
    fun favoriteScreenError() {
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    FavoriteScreen(
                        navigateToDetail = {},
                        viewModel = MockFavoriteViewModel(repository, true)
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithTag("errorRoom").assertExists()
    }

    @Test
    fun favoriteScreenLoadDataSuccess() {
        val anime1 = AnimeEntity(40357, "https://cdn.myanimelist.net/images/anime/1188/136926l.jpg", "Tate no Yuusha no Nariagari Season 3", "盾の勇者の成り上がり Season 3", "Currently Airing", 7.71, "fall", 2023)
        val anime2 = AnimeEntity(53887, "https://cdn.myanimelist.net/images/anime/1506/138982l.jp", "Spy x Family Season 2", "SPY×FAMILY Season 2", "Currently Airing", 8.38, "fall", 2023)
        val anime3 = AnimeEntity(54595, "https://cdn.myanimelist.net/images/anime/1938/138295l.jpg", "Kage no Jitsuryokusha ni Naritakute! 2nd Season", "陰の実力者になりたくて！ 2nd Season", "Currently Airing", 8.72, "fall", 2023)


        runBlocking {
            animeDao.insert(anime1)
            animeDao.insert(anime2)
            animeDao.insert(anime3)
        }

        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    FavoriteScreen(
                        navigateToDetail = {},
                        viewModel = MockFavoriteViewModel(repository, false)
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithText("Spy x Family Season 2").assertExists()
    }

    @After
    fun tearDown() {
        database.close()
    }
}