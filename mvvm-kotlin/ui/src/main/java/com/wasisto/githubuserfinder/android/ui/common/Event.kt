package com.wasisto.githubuserfinder.android.ui.common

class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled() = if (hasBeenHandled) null else content.also { hasBeenHandled = true }

    fun peekContent() = content
}