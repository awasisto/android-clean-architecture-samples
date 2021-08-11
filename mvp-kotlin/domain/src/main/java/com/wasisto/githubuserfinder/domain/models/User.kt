package com.wasisto.githubuserfinder.domain.models

data class User (
    var login: String,
    var avatarUrl: String,
    var name: String? = null,
    var company: String? = null,
    var blog: String? = null,
    var location: String? = null
)