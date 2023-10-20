package com.kotlin.animu.ui.screen.home

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
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    private val mockWebServer = MockWebServer()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
    }

    @Test
    fun homeScreenLoadDataSuccess() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile(composeTestRule, "list_data.json"))
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    HomeScreen(
                        navigateToDetail = {}
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithText("Spy x Family Season 2").assertExists()
    }

    @Test
    fun homeScreenLoading() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile(composeTestRule, "list_data.json"))
            .throttleBody(1024, 4, TimeUnit.SECONDS)
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    HomeScreen(
                        navigateToDetail = {}
                    )
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithTag("loading").assertExists()
    }

    @Test
    fun homeScreenError() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody(JsonConverter.readStringFromFile(composeTestRule, "error_response.json"))
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    HomeScreen(
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
    fun homeScreenDataEmpty() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{\"data\": []}")
        mockWebServer.enqueue(mockResponse)
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    HomeScreen(
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