package com.cypress.bbcnewsapplication

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test


class BbcNewsInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    fun hasTextContainingSubstring(substring: String, ignoreCase: Boolean = false) =
        SemanticsMatcher("Text contains '$substring'") { node ->
            val text = node.config.getOrNull(SemanticsProperties.Text)
                ?.joinToString("") { it.text }
            text?.contains(substring, ignoreCase) ?: false
        }

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.cypress.bbcnewsapplication", appContext.packageName)
        composeRule.waitUntil(timeoutMillis = 20000) {
            composeRule.onAllNodesWithTag("source_tag").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("ABC News").assertExists()
        composeRule.onNodeWithText("ABC News").performClick()

        composeRule.waitUntil(timeoutMillis = 20000) {
            composeRule.onAllNodes(hasTextContainingSubstring("profits")).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onAllNodes(hasTextContainingSubstring("profits"))
            .onFirst()
            .performClick()

    }
}