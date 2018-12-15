/*
 * Copyright (c) 2018 Andika Wasisto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wasisto.githubuserfinder.ui.userdetails

import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.domain.GetUserUseCase
import com.wasisto.githubuserfinder.util.logging.LoggingHelper

class UserDetailsPresenterImpl(
    private val username: String,
    private val view: UserDetailsView,
    private val getUserUseCase: GetUserUseCase,
    private val loggingHelper: LoggingHelper
) : UserDetailsPresenter {

    companion object {

        private const val TAG = "UserDetailsPresenterImp"
    }

    override fun onViewReady() {
        view.hideAvatar()
        view.hideName()
        view.hideUsername()
        view.hideCompany()
        view.hideLocation()
        view.hideBlog()

        view.showLoadingIndicator()

        getUserUseCase.execute(
            username,
            onSuccess = { user ->
                view.hideLoadingIndicator()

                view.showAvatar(user.avatarUrl)
                view.showUsername(user.login)

                user.name?.let { view.showName(it) }
                user.company?.let { view.showCompany(it) }
                user.location?.let { view.showLocation(it) }
                user.blog?.let { view.showBlog(it) }
            },
            onError = { error ->
                loggingHelper.error(TAG, "An error occurred while getting a user", error)

                view.hideLoadingIndicator()

                view.showToast(R.string.an_error_occurred)
                view.closeActivity()
            }
        )
    }

    override fun onBlogClick() {
        view.showLoadingIndicator()

        getUserUseCase.execute(
            username,
            onSuccess = { user ->
                view.hideLoadingIndicator()
                view.openBrowser(user.blog!!)
            },
            onError = { error ->
                loggingHelper.error(TAG, "An error occurred while getting a user", error)

                view.hideLoadingIndicator()

                view.showToast(R.string.an_error_occurred)
            }
        )
    }
}
