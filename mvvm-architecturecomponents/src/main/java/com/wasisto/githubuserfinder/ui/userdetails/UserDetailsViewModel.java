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

package com.wasisto.githubuserfinder.ui.userdetails;

import androidx.lifecycle.*;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.domain.UseCase;
import com.wasisto.githubuserfinder.model.User;
import com.wasisto.githubuserfinder.domain.GetUserUseCase;
import com.wasisto.githubuserfinder.ui.Event;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

public class UserDetailsViewModel extends ViewModel {

    private static final String TAG = "UserDetailsViewModel";

    private MediatorLiveData<UseCase.Result<User>> getUserResult = new MediatorLiveData<>();

    private LiveData<Boolean> isLoading;

    private LiveData<User> user;

    private LiveData<Boolean> shouldShowUserDetails;

    private MutableLiveData<Event<String>> openBrowserEvent = new MutableLiveData<>();

    private MediatorLiveData<Event<Integer>> showToastEvent = new MediatorLiveData<>();

    private MediatorLiveData<Event<Void>> closeActivityEvent = new MediatorLiveData<>();

    public UserDetailsViewModel(String username, GetUserUseCase getUserUseCase, LoggingHelper loggingHelper) {
        getUserResult.addSource(
                getUserUseCase.executeAsync(username),
                result -> {
                    if (result.status == UseCase.Result.Status.ERROR) {
                        loggingHelper.error(TAG, "An error occurred while getting a user", result.error);
                    }

                    getUserResult.setValue(result);
                }
        );

        isLoading = Transformations.map(
                getUserResult,
                result -> result.status == UseCase.Result.Status.LOADING
        );

        user = Transformations.map(
                getUserResult,
                result -> result.data
        );

        shouldShowUserDetails = Transformations.map(
                getUserResult,
                result -> result.status == UseCase.Result.Status.SUCCESS
        );

        showToastEvent.addSource(
                getUserResult,
                result -> {
                    if (result.status == UseCase.Result.Status.ERROR) {
                        showToastEvent.setValue(new Event<>(R.string.an_error_occurred));
                    }
                }
        );

        closeActivityEvent.addSource(
                getUserResult,
                result -> {
                    if (result.status == UseCase.Result.Status.ERROR) {
                        closeActivityEvent.setValue(new Event<>(null));
                    }
                }
        );
    }

    public void onBlogClick() {
        User u = user.getValue();
        if (u != null) {
            openBrowserEvent.setValue(new Event<>(u.getBlog()));
        }
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Boolean> getShouldShowUserDetails() {
        return shouldShowUserDetails;
    }

    public MutableLiveData<Event<String>> getOpenBrowserEvent() {
        return openBrowserEvent;
    }

    public MediatorLiveData<Event<Integer>> getShowToastEvent() {
        return showToastEvent;
    }

    public MediatorLiveData<Event<Void>> getCloseActivityEvent() {
        return closeActivityEvent;
    }
}
