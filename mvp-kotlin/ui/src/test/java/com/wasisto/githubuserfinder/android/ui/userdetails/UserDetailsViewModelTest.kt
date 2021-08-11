package com.wasisto.githubuserfinder.android.ui.userdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wasisto.githubuserfinder.android.MainCoroutineRule
import com.wasisto.githubuserfinder.android.NoOpLogger
import com.wasisto.githubuserfinder.domain.models.User
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase
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
class UserDetailsViewModelTest {

    private lateinit var userDetailsPresenter: UserDetailsPresenter

    @MockK
    private lateinit var userDetailsView: UserDetailsView

    @MockK
    private lateinit var getUserUseCase: GetUserUseCase

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userDetailsPresenter = UserDetailsPresenter(
            userDetailsView,
            getUserUseCase,
            NoOpLogger,
            mainCoroutineRule.dispatcher,
            mainCoroutineRule.dispatcher,
            mainCoroutineRule
        )
    }

    @Test
    fun testLoad() = mainCoroutineRule.runBlockingTest {
        val username = "octocat"
        val user = User(
            login = username,
            avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
            blog = "https://github.blog"
        )

        every { getUserUseCase(username) }.returns(user)

        userDetailsPresenter.load(username)

        verify { userDetailsView.showUser(user) }
    }

    @Test
    fun testLoad_getUserThrowsException() = mainCoroutineRule.runBlockingTest {
        val username = "octocat"

        every { getUserUseCase(username) }.throws(Exception())

        userDetailsPresenter.load(username)

        verify { userDetailsView.showErrorToast() }
        verify { userDetailsView.closeActivity() }
    }

    @Test
    fun testOnBlogClick() = mainCoroutineRule.runBlockingTest {
        val username = "octocat"
        val user = User(
            login = username,
            avatarUrl = "https://avatars.githubusercontent.com/u/583231?v=4",
            blog = "https://github.blog"
        )
        every { getUserUseCase(username) }.returns(user)
        userDetailsPresenter.load(username)

        userDetailsPresenter.onBlogClick()

        verify{ userDetailsView.openBrowser(user.blog!!) }
    }
}