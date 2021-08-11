package com.wasisto.githubuserfinder.domain.interfaces

import com.wasisto.githubuserfinder.domain.models.SearchUsersResult
import com.wasisto.githubuserfinder.domain.models.User

interface GithubDataSource {

    fun searchUsers(query: String): SearchUsersResult

    fun getUser(username: String): User
}