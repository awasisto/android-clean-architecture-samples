package com.wasisto.githubuserfinder.domain.usecases

import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource
import com.wasisto.githubuserfinder.domain.models.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class GetUserUseCaseTest {

    private lateinit var getUserUseCase: GetUserUseCase

    @MockK
    private lateinit var githubDataSource: GithubDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        getUserUseCase = GetUserUseCase(githubDataSource)
    }

    @Test
    fun testInvoke() {
        val username = "octocat"
        val user = User(
            login = username,
            avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
            blog = "https://github.blog"
        )

        every { githubDataSource.getUser(username) }.returns(user)

        assertEquals(user, getUserUseCase(username))
    }
}