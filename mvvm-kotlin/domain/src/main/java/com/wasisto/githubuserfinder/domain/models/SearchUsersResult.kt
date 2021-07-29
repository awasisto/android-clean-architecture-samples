package com.wasisto.githubuserfinder.domain.models

data class SearchUsersResult (
    var totalCount: Long,
    var items: List<Item>
) {
    data class Item(
        var login: String,
        var avatarUrl: String? = null
    )
}