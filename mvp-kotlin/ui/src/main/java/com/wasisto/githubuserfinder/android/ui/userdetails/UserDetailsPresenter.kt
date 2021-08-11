package com.wasisto.githubuserfinder.android.ui.userdetails

import com.wasisto.githubuserfinder.common.Logger
import com.wasisto.githubuserfinder.domain.models.User
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UserDetailsPresenter(
    private val view: UserDetailsView,
    private val getUserUseCase: GetUserUseCase,
    private val logger: Logger,
    private val ioDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
    private val coroutineScope: CoroutineScope
)  {

    private lateinit var user: User

    fun load(username: String) = coroutineScope.launch(mainDispatcher) {
        view.showLoadingIndicator()

        launch(ioDispatcher) {
            try {
                user = getUserUseCase(username)

                logger.debug("user: $user")

                launch(mainDispatcher) {
                    view.hideLoadingIndicator()
                    view.showUser(user)
                }
            } catch (e: Exception) {
                logger.warn("An error occurred while getting user details", e)

                launch(mainDispatcher) {
                    view.showErrorToast()
                    view.closeActivity()
                }
            }
        }
    }

    fun onBlogClick() = coroutineScope.launch(mainDispatcher) {
        user.blog?.let {
            view.openBrowser(it)
        }
    }
}