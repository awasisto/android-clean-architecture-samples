package com.wasisto.githubuserfinder.android.ui.userdetails;

import com.wasisto.githubuserfinder.domain.models.User;

public interface UserDetailsView {

    void showLoadingIndicator();

    void hideLoadingIndicator();

    void showUser(User user);

    void openBrowser(String url);

    void showErrorToast();

    void closeActivity();
}
