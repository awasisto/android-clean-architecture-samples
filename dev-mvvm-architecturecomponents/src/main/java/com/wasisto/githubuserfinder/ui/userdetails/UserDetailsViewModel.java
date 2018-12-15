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

package com.wasisto.githubuserfinder.ui.userdetails;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.wasisto.githubuserfinder.Callback;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.model.User;
import com.wasisto.githubuserfinder.domain.GetUserUseCase;
import com.wasisto.githubuserfinder.ui.Event;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

public class UserDetailsViewModel extends ViewModel {

    private static final String TAG = "UserDetailsViewModel";

    private MutableLiveData<User> user = new MutableLiveData<>();

    private MutableLiveData<Event<Integer>> errorMessageEvent = new MutableLiveData<>();

    private String username;

    private GetUserUseCase getUserUseCase;

    private LoggingHelper loggingHelper;

    public UserDetailsViewModel(String username, GetUserUseCase getUserUseCase, LoggingHelper loggingHelper) {
        this.username = username;
        this.getUserUseCase = getUserUseCase;
        this.loggingHelper = loggingHelper;

        this.getUserUseCase.execute(this.username, new Callback<User>() {
            @Override
            public void onSuccess(User user) {
                UserDetailsViewModel.this.user.setValue(user);
            }

            @Override
            public void onError(Throwable error) {
                UserDetailsViewModel.this.loggingHelper.error(TAG, "An error occurred while getting a user",
                        error);

                Event<Integer> errorEvent = new Event<>();
                errorEvent.setData(R.string.an_error_occurred);

                UserDetailsViewModel.this.errorMessageEvent.setValue(errorEvent);
            }
        });
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public MutableLiveData<Event<Integer>> getErrorMessageEvent() {
        return errorMessageEvent;
    }
}
