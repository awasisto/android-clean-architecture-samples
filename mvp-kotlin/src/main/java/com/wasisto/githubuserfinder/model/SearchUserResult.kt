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

package com.wasisto.githubuserfinder.model

data class SearchUserResult (
    var totalCount: Long,
    var isIncompleteResults: Boolean,
    var items: List<Item>
) {
    data class Item(
        var login: String,
        var id: Long?,
        var nodeId: String?,
        var avatarUrl: String?,
        var gravatarId: String?,
        var url: String?,
        var htmlUrl: String?,
        var followersUrl: String?,
        var followingUrl: String?,
        var gistsUrl: String?,
        var starredUrl: String?,
        var subscriptionsUrl: String?,
        var organizationsUrl: String?
    )
}
