package com.wasisto.githubuserfinder.android.ui.userdetails

import com.wasisto.githubuserfinder.domain.models.User

interface UserDetailsView {

    fun showLoadingIndicator()

    fun hideLoadingIndicator()

    fun showUser(user: User)

    fun openBrowser(url: String)

    fun showErrorToast()

    fun closeActivity()
}