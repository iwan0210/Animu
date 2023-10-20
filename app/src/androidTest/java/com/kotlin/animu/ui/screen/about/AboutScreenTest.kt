package com.kotlin.animu.ui.screen.about

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kotlin.animu.R
import com.kotlin.animu.ui.theme.AnimuTheme
import com.kotlin.animu.utils.onNodeWithStringId
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AboutScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun aboutScreenSuccess() {
        composeTestRule.setContent {
            AnimuTheme {
                Surface {
                    AboutScreen()
                }
            }
        }
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("currentLabelExists")
        composeTestRule.onNodeWithStringId(R.string.profile_email).assertExists()
    }
}