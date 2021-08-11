package com.wasisto.githubuserfinder.domain.usecases

import com.wasisto.githubuserfinder.domain.NoOpLogger
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource
import com.wasisto.githubuserfinder.domain.interfaces.SearchHistoryDataSource
import com.wasisto.githubuserfinder.domain.models.SearchHistoryItem
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class SearchUsersUseCaseTest {

    private lateinit var searchUsersUseCase: SearchUsersUseCase

    @MockK
    private lateinit var githubDataSource: GithubDataSource

    @MockK
    private lateinit var searchHistoryDataSource: SearchHistoryDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchUsersUseCase = SearchUsersUseCase(githubDataSource, searchHistoryDataSource, NoOpLogger)
    }

    @Test
    fun testInvoke() {
        val query = "john"
        val searchResult = SearchUsersResult(
            totalCount = 2,
            items = listOf(
                SearchUsersResult.Item("johnsmith"),
                SearchUsersResult.Item("johndoe")
            )
        )

        every { githubDataSource.searchUsers(query) }.returns(searchResult)

        assertEquals(searchResult, searchUsersUseCase(query))
        verify { searchHistoryDataSource.add(SearchHistoryItem(query = query)) }
    }

    @Test
    fun testInvoke_addSearchHistoryItemThrowsException() {
        val query = "john"
        val searchResult = SearchUsersResult(
            totalCount = 2,
            items = listOf(
                SearchUsersResult.Item("johnsmith"),
                SearchUsersResult.Item("johndoe")
            )
        )

        every { githubDataSource.searchUsers(query) }.returns(searchResult)

        val searchHistoryItemSlot = slot<SearchHistoryItem>()

        every { searchHistoryDataSource.add(capture(searchHistoryItemSlot)) }.throws(Exception())

        assertEquals(searchResult, searchUsersUseCase(query))
        verify { searchHistoryDataSource.add(SearchHistoryItem(query = query)) }
    }
}