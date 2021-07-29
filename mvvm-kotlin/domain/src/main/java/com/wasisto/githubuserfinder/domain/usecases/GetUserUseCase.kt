package com.wasisto.githubuserfinder.domain.usecases

import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource

class GetUserUseCase(private val githubDataSource: GithubDataSource) {
    operator fun invoke(username: String) = githubDataSource.getUser(username)
}