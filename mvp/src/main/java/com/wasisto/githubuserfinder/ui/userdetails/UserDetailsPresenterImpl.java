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

import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.usecase.UseCase;
import com.wasisto.githubuserfinder.model.User;
import com.wasisto.githubuserfinder.usecase.GetUserUseCase;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;

public class UserDetailsPresenterImpl implements UserDetailsPresenter {

    private static final String TAG = "UserDetailsPresenterImp";

    private String username;

    private UserDetailsView view;

    private GetUserUseCase getUserUseCase;

    private LoggingHelper loggingHelper;

    public UserDetailsPresenterImpl(String username, UserDetailsView view, GetUserUseCase getUserUseCase,
                                    LoggingHelper loggingHelper) {
        this.username = username;
        this.view = view;
        this.getUserUseCase = getUserUseCase;
        this.loggingHelper = loggingHelper;
    }

    @Override
    public void onViewCreated() {
        view.hideAvatar();
        view.hideName();
        view.hideUsername();
        view.hideCompany();
        view.hideLocation();
        view.hideBlog();

        view.showLoadingIndicator();

        getUserUseCase.executeAsync(username, new UseCase.Callback<User>() {
            @Override
            public void onSuccess(User user) {
                loggingHelper.debug(TAG, String.format("user: %s", user));

                view.hideLoadingIndicator();

                view.showAvatar(user.getAvatarUrl());
                view.showUsername(user.getLogin());

                if (user.getName() != null) {
                    view.showName(user.getName());
                }

                if (user.getCompany() != null) {
                    view.showCompany(user.getCompany());
                }

                if (user.getLocation() != null) {
                    view.showLocation(user.getLocation());
                }

                if (user.getBlog() != null) {
                    view.showBlog(user.getBlog());
                }
            }

            @Override
            public void onError(Throwable error) {
                loggingHelper.warn(TAG, "An error occurred while getting a user", error);

                view.hideLoadingIndicator();

                view.showToast(R.string.an_error_occurred);
                view.closeActivity();
            }
        });
    }

    @Override
    public void onBlogClick() {
        view.showLoadingIndicator();

        getUserUseCase.executeAsync(username, new UseCase.Callback<User>() {
            @Override
            public void onSuccess(User user) {
                loggingHelper.debug(TAG, String.format("user: %s", user));

                view.hideLoadingIndicator();
                view.openBrowser(user.getBlog());
            }

            @Override
            public void onError(Throwable error) {
                loggingHelper.warn(TAG, "An error occurred while getting a user", error);

                view.hideLoadingIndicator();

                view.showToast(R.string.an_error_occurred);
            }
        });
    }
}
