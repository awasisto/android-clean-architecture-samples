package com.wasisto.githubuserfinder.domain.usecases;

import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource;
import com.wasisto.githubuserfinder.domain.models.User;

public class GetUserUseCase {

    private GithubDataSource githubDataSource;

    public GetUserUseCase(GithubDataSource githubDataSource) {
        this.githubDataSource = githubDataSource;
    }

    public User execute(String username) throws Exception {
        return githubDataSource.getUser(username);
    }
}
