package com.wasisto.githubuserfinder.android.ui.userdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wasisto.githubuserfinder.android.MainCoroutineRule
import com.wasisto.githubuserfinder.android.NoOpLogger
import com.wasisto.githubuserfinder.domain.models.User
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDetailsViewModelTest {

    private lateinit var userDetailsViewModel: UserDetailsViewModel

    @MockK
    private lateinit var getUserUseCase: GetUserUseCase

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userDetailsViewModel = UserDetailsViewModel(
            getUserUseCase,
            NoOpLogger,
            mainCoroutineRule.dispatcher,
            mainCoroutineRule.dispatcher
        )
    }

    @Test
    fun testLoad() {
        val username = "octocat"
        val user = User(
            login = username,
            avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
            blog = "https://github.blog"
        )

        every { getUserUseCase(username) }.returns(user)

        userDetailsViewModel.load(username)

        assertEquals(user, userDetailsViewModel.user.value)
    }

    @Test
    fun testLoad_getUserThrowsException() {
        val username = "octocat"

        every { getUserUseCase(username) }.throws(Exception())

        userDetailsViewModel.load(username)

        assertNotNull(userDetailsViewModel.showErrorToastEvent.value)
        assertNotNull(userDetailsViewModel.closeActivityEvent.value)
    }

    @Test
    fun testOnBlogClick() {
        val username = "octocat"
        val user = User(
            login = username,
            avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
            blog = "https://github.blog"
        )
        every { getUserUseCase(username) }.returns(user)
        userDetailsViewModel.load(username)

        userDetailsViewModel.onBlogClick()

        assertNotNull(userDetailsViewModel.openBrowserEvent.value)
    }
}