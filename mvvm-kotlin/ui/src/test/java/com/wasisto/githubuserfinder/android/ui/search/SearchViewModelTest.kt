package com.wasisto.githubuserfinder.android.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wasisto.githubuserfinder.android.MainCoroutineRule
import com.wasisto.githubuserfinder.android.NoOpLogger
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult
import com.wasisto.githubuserfinder.domain.usecases.GetSearchHistoryUseCase
import com.wasisto.githubuserfinder.domain.usecases.SearchUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel

    @MockK
    private lateinit var searchUsersUseCase: SearchUsersUseCase

    @MockK
    private lateinit var getSearchHistoryUseCase: GetSearchHistoryUseCase

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchViewModel = SearchViewModel(
            searchUsersUseCase,
            getSearchHistoryUseCase,
            NoOpLogger,
            mainCoroutineRule.dispatcher,
            mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun testLoad() = mainCoroutineRule.runBlockingTest {
        val searchHistory = listOf(
            SearchHistoryItem(id = 1, query = "john"),
            SearchHistoryItem(id = 2, query = "jane")
        )

        every { getSearchHistoryUseCase() }.returns(searchHistory)

        searchViewModel.load()

        assertEquals(searchHistory.sortedByDescending { it.id }, searchViewModel.searchHistoryItems.value)
        assertEquals(true, searchViewModel.shouldShowHistory.value)
    }

    @Test
    fun testLoad_getSearchHistoryThrowsException() = mainCoroutineRule.runBlockingTest {
        every { getSearchHistoryUseCase() }.throws(Exception())

        searchViewModel.load()

        assertNotNull(searchViewModel.showErrorToastEvent.value)
    }

    @Test
    fun testOnSearchButtonClick() = mainCoroutineRule.runBlockingTest {
        val query = "john"
        val searchResult = SearchUsersResult(
            totalCount = 2,
            items = listOf(
                SearchUsersResult.Item(login = "johnsmith"),
                SearchUsersResult.Item(login = "johndoe")
            )
        )

        every { searchUsersUseCase(query) }.returns(searchResult)

        searchViewModel.query.value = query
        searchViewModel.onSearchButtonClick()

        assertEquals(searchResult.items, searchViewModel.searchResultItems.value)
    }


    @Test
    fun testOnSearchButtonClick_noResults() = mainCoroutineRule.runBlockingTest {
        val query = "john"
        val searchResult = SearchUsersResult(
            totalCount = 0,
            items = emptyList()
        )

        every { searchUsersUseCase(query) }.returns(searchResult)

        searchViewModel.query.value = query
        searchViewModel.onSearchButtonClick()

        assertEquals(true, searchViewModel.shouldShowNoResultsText.value)
    }

    @Test
    fun testOnSearchButtonClick_searchUsersThrowsException() = mainCoroutineRule.runBlockingTest {
        val query = "john"

        every { searchUsersUseCase(query) }.throws(Exception())

        searchViewModel.query.value = query
        searchViewModel.onSearchButtonClick()

        assertNotNull(searchViewModel.showErrorToastEvent.value)
    }

    @Test
    fun testOnResultItemClick() = mainCoroutineRule.runBlockingTest {
        val query = "john"
        val searchResultItem = SearchUsersResult.Item(login = "johnsmith")
        every { searchUsersUseCase(query) }.returns(
            SearchUsersResult(
                totalCount = 1,
                items = listOf(searchResultItem)
            )
        )
        searchViewModel.onSearchButtonClick()

        searchViewModel.onResultItemClick(searchResultItem)

        assertEquals(searchResultItem.login, searchViewModel.openUserDetailsActivityEvent.value?.getContentIfNotHandled())
    }

    @Test
    fun testOnSearchHistoryItemClick() = mainCoroutineRule.runBlockingTest {
        val query = "john"
        val searchHistoryItem = SearchHistoryItem(id = 1, query = query)
        val searchResult = SearchUsersResult(
            totalCount = 2,
            items = listOf(
                SearchUsersResult.Item(login = "johnsmith"),
                SearchUsersResult.Item(login = "johndoe")
            )
        )
        every { getSearchHistoryUseCase() }.returns(listOf(searchHistoryItem))
        every { searchUsersUseCase(query) }.returns(searchResult)
        searchViewModel.load()

        searchViewModel.onSearchHistoryItemClick(searchHistoryItem)

        assertEquals(query, searchViewModel.query.value)
        assertEquals(searchResult.items, searchViewModel.searchResultItems.value)
    }
}