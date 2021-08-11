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
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    private lateinit var searchPresenter: SearchPresenter

    @MockK
    private lateinit var searchView: SearchView

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
        searchPresenter = SearchPresenter(
            searchView,
            searchUsersUseCase,
            getSearchHistoryUseCase,
            NoOpLogger,
            mainCoroutineRule.dispatcher,
            mainCoroutineRule.dispatcher,
            mainCoroutineRule
        )
    }

    @Test
    fun testLoad() = mainCoroutineRule.runBlockingTest {
        val searchHistory = listOf(
            SearchHistoryItem(id = 1, query = "john"),
            SearchHistoryItem(id = 2, query = "jane")
        )

        every { getSearchHistoryUseCase() }.returns(searchHistory)

        searchPresenter.load()

        verify { searchView.showSearchHistory(searchHistory.sortedByDescending { it.id }) }
    }

    @Test
    fun testLoad_getSearchHistoryThrowsException() = mainCoroutineRule.runBlockingTest {
        every { getSearchHistoryUseCase() }.throws(Exception())

        searchPresenter.load()

        verify { searchView.showErrorToast() }
    }

    @Test
    fun testSearch() = mainCoroutineRule.runBlockingTest {
        val query = "john"
        val searchResult = SearchUsersResult(
            totalCount = 2,
            items = listOf(
                SearchUsersResult.Item(login = "johnsmith"),
                SearchUsersResult.Item(login = "johndoe")
            )
        )

        every { searchUsersUseCase(query) }.returns(searchResult)

        searchPresenter.search(query)

        verify { searchView.showSearchResult(searchResult.items) }
    }

    @Test
    fun testSearch_noResults() = mainCoroutineRule.runBlockingTest {
        val query = "john"
        val searchResult = SearchUsersResult(
            totalCount = 0,
            items = emptyList()
        )

        every { searchUsersUseCase(query) }.returns(searchResult)

        searchPresenter.search(query)

        verify { searchView.showNoResultsText() }
    }

    @Test
    fun testSearch_searchUsersThrowsException() = mainCoroutineRule.runBlockingTest {
        val searchHistory = listOf(
            SearchHistoryItem(id = 1, query = "john"),
            SearchHistoryItem(id = 2, query = "jane")
        )
        every { getSearchHistoryUseCase() }.returns(searchHistory)
        searchPresenter.load()

        val query = "john"

        every { searchUsersUseCase(query) }.throws(Exception())

        searchPresenter.search(query)

        verify { searchView.showErrorToast() }
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
        searchPresenter.search(query)

        searchPresenter.onResultItemClick(searchResultItem)

        verify { searchView.openUserDetailsActivity(searchResultItem.login) }
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

        searchPresenter.onSearchHistoryItemClick(searchHistoryItem)

        verify { searchView.setQuery(query) }
        verify { searchView.showSearchResult(searchResult.items) }
    }
}