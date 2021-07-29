package com.wasisto.githubuserfinder.domain.usecases

import com.wasisto.githubuserfinder.common.Logger
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

    @MockK
    private lateinit var logger: Logger

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        searchUsersUseCase = SearchUsersUseCase(githubDataSource, searchHistoryDataSource, logger)
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

        val searchHistoryItemSlot = slot<SearchHistoryItem>()

        every { searchHistoryDataSource.add(capture(searchHistoryItemSlot)) }

        assertEquals(searchResult, searchUsersUseCase(query))
        assertEquals(query, searchHistoryItemSlot.captured.query)
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
        assertEquals(query, searchHistoryItemSlot.captured.query)
    }
}