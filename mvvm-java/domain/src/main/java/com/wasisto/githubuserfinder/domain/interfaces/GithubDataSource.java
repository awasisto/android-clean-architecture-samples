package com.wasisto.githubuserfinder.domain.interfaces;

import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.models.User;

public interface GithubDataSource {

    SearchUsersResult searchUsers(String query) throws Exception;

    User getUser(String username) throws Exception;
}