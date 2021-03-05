/*
 * Copyright (c) 2019 Andika Wasisto
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

import androidx.lifecycle.*
import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.usecase.GetUserUseCase
import com.wasisto.githubuserfinder.usecase.UseCase
import com.wasisto.githubuserfinder.ui.Event

import com.wasisto.githubuserfinder.model.User
import com.wasisto.githubuserfinder.util.logging.LoggingHelper

class UserDetailsViewModel(
        username: String,
        getUserUseCase: GetUserUseCase,
        loggingHelper: LoggingHelper
) : ViewModel() {

    companion object {
        private const val TAG = "UserDetailsViewModel"
    }

    private val getUserResult = MediatorLiveData<UseCase.Result<User>>()

    val isLoading: LiveData<Boolean>

    val user: LiveData<User>

    val shouldShowUserDetails: LiveData<Boolean>

    val openBrowserEvent = MutableLiveData<Event<String>>()

    val showToastEvent = MediatorLiveData<Event<Int>>()

    val closeActivityEvent = MediatorLiveData<Event<Unit>>()

    init {
        getUserResult.addSource(getUserUseCase.executeAsync(username)) { result ->
            if (result is UseCase.Result.Success) {
                loggingHelper.debug(TAG, "result.data: ${result.data}")
            } else if (result is UseCase.Result.Error) {
                loggingHelper.warn(TAG, "An error occurred while getting a user", result.error)
            }

            getUserResult.value = result
        }

        isLoading = Transformations.map(getUserResult) { result ->
            result is UseCase.Result.Loading
        }

        user = Transformations.map(getUserResult) { result ->
            (result as? UseCase.Result.Success)?.data
        }

        shouldShowUserDetails = Transformations.map(getUserResult) { result ->
            result is UseCase.Result.Success
        }

        showToastEvent.addSource(getUserResult) { result ->
            if (result is UseCase.Result.Error) {
                showToastEvent.value = Event(R.string.an_error_occurred)
            }
        }

        closeActivityEvent.addSource(getUserResult) { result ->
            if (result is UseCase.Result.Error) {
                closeActivityEvent.value = Event(Unit)
            }
        }
    }

    fun onBlogClick() {
        user.value?.blog?.let { blog ->
            openBrowserEvent.value = Event(blog)
        }
    }
}
