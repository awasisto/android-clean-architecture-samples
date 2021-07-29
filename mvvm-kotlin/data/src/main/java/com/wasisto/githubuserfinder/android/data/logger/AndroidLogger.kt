package com.wasisto.githubuserfinder.android.data.logger

import android.util.Log
import com.wasisto.githubuserfinder.common.Logger
import kotlin.reflect.KClass

class AndroidLogger(cls: KClass<*>) : Logger {

    companion object {
        private const val MAX_TAG_LENGTH = 23
    }

    private val tag: String =
        if (cls.java.simpleName.length > MAX_TAG_LENGTH)
            cls.java.simpleName.substring(0, MAX_TAG_LENGTH - 1)
        else
            cls.java.simpleName

    override fun trace(message: String) {
        Log.v(tag, message)
    }

    override fun trace(message: String, throwable: Throwable) {
        Log.v(tag, message, throwable)
    }

    override fun debug(message: String) {
        Log.d(tag, message)
    }

    override fun debug(message: String, throwable: Throwable) {
        Log.d(tag, message, throwable)
    }

    override fun info(message: String) {
        Log.i(tag, message)
    }

    override fun info(message: String, throwable: Throwable) {
        Log.i(tag, message, throwable)
    }

    override fun warn(message: String) {
        Log.w(tag, message)
    }

    override fun warn(message: String, throwable: Throwable) {
        Log.w(tag, message, throwable)
    }

    override fun error(message: String) {
        Log.e(tag, message)
    }

    override fun error(message: String, throwable: Throwable) {
        Log.e(tag, message, throwable)
    }

    override fun fatal(message: String) {
        Log.wtf(tag, message)
    }

    override fun fatal(message: String, throwable: Throwable) {
        Log.wtf(tag, message, throwable)
    }
}