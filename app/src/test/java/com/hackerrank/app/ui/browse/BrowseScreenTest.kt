package com.hackerrank.app.ui.browse

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.*
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.hackerrank.app.core.LocalLocaleManager
import com.hackerrank.app.core.LocalThemeManager
import com.hackerrank.app.core.LocaleManager
import com.hackerrank.app.core.ThemeManager
import com.hackerrank.app.core.ThemeMode
import com.hackerrank.app.domain.model.DataStructure
import com.hackerrank.app.domain.model.DataStructureCategory
import com.hackerrank.app.domain.model.Difficulty
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class BrowseScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setBrowseContent(
        viewModel: BrowseViewModel,
        onStructureClick: (String) -> Unit = {},
    ) {
        val themeManager: ThemeManager = mockk(relaxed = true)
        val localeManager: LocaleManager = mockk(relaxed = true)
        every { themeManager.themeMode } returns MutableStateFlow(ThemeMode.SYSTEM)
        every { localeManager.getCurrentCode() } returns "en"

        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalThemeManager provides themeManager,
                LocalLocaleManager provides localeManager,
            ) {
                MaterialTheme {
                    BrowseScreen(onStructureClick = onStructureClick, viewModel = viewModel)
                }
            }
        }
    }

    @Test
    fun loadedState_displaysCategoryHeaders() {
        val viewModel: BrowseViewModel = mockk()
        val groupedStructures =
            mapOf(
                DataStructureCategory.LINEAR to
                    listOf(
                        DataStructure("1", "Linked List", "linked-list", DataStructureCategory.LINEAR, "Linear...", emptyMap(), emptyList(), null, "", Difficulty.EASY),
                    ),
                DataStructureCategory.TREES to
                    listOf(
                        DataStructure("2", "BST", "bst", DataStructureCategory.TREES, "Tree...", emptyMap(), emptyList(), null, "", Difficulty.MEDIUM),
                    ),
            )
        every { viewModel.uiState } returns
            MutableStateFlow(
                BrowseUiState.Loaded(groupedStructures = groupedStructures, progressMap = emptyMap()),
            )
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        setBrowseContent(viewModel)

        composeTestRule.onNodeWithText("Linked List", useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText("BST", useUnmergedTree = true).assertExists()
    }

    @Test
    fun tappingCard_triggersOnStructureClick() {
        val viewModel: BrowseViewModel = mockk()
        val groupedStructures =
            mapOf(
                DataStructureCategory.LINEAR to
                    listOf(
                        DataStructure("1", "Linked List", "linked-list", DataStructureCategory.LINEAR, "Linear...", emptyMap(), emptyList(), null, "", Difficulty.EASY),
                    ),
            )
        every { viewModel.uiState } returns
            MutableStateFlow(
                BrowseUiState.Loaded(groupedStructures = groupedStructures, progressMap = emptyMap()),
            )
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        var clickedSlug: String? = null
        setBrowseContent(viewModel, onStructureClick = { clickedSlug = it })

        composeTestRule.onNodeWithText("Linked List", useUnmergedTree = true).performClick()
        assert(clickedSlug == "linked-list")
    }

    @Test
    fun loadingState_displaysLoadingIndicator() {
        val viewModel: BrowseViewModel = mockk()
        every { viewModel.uiState } returns MutableStateFlow(BrowseUiState.Loading)
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        setBrowseContent(viewModel)

        composeTestRule.onNodeWithTag("loadingIndicator").assertExists()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        val viewModel: BrowseViewModel = mockk()
        every { viewModel.uiState } returns MutableStateFlow(BrowseUiState.Error("Something went wrong"))
        every { viewModel.isRefreshing } returns MutableStateFlow(false)

        setBrowseContent(viewModel)

        composeTestRule.onNodeWithText("Error").assertExists()
    }
}
