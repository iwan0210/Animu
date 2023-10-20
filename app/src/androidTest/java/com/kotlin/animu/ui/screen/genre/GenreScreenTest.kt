package com.kotlin.animu.ui.screen.genre

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kotlin.animu.R
import com.kotlin.animu.data.remote.ApiConfig
import com.kotlin.animu.ui.theme.AnimuTheme
import com.kotlin.animu.utils.JsonConverter
import com.kotlin.animu.utils.onNodeWithStringId
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GenreScreenTest {
    private val mockWebServer = MockWebServer()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
    }

    @Test
    fun test1_genreScreenLoadDataSuccess(): Unit = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile(composeTestRule, "list_data.json"))
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    GenreScreen(
                        genreId = 1,
                        navigateToDetail = {}
                    )
                }
            }
        }
        //
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        delay(2000)
        composeTestRule.onNodeWithText("Spy x Family Season 2").assertExists()
    }

    @Test
    fun test2_genreScreenLoading() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile(composeTestRule, "list_data.json"))
            .throttleBody(1024, 4, TimeUnit.SECONDS)
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    GenreScreen(
                        genreId = 1,
                        navigateToDetail = {}
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @Test
    fun test3_genreScreenError() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody(JsonConverter.readStringFromFile(composeTestRule, "error_response.json"))
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    GenreScreen(
                        genreId = 1,
                        navigateToDetail = {}
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.error_message,
                "Exception Message"
            )
        )
    }

    @Test
    fun test4_genreScreenDataEmpty() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"data\": []}")
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    GenreScreen(
                        genreId = 1,
                        navigateToDetail = {}
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithStringId(R.string.no_anime_found)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}