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

package com.wasisto.githubuserfinder.util.logging

import android.util.Log

class LoggingHelperImpl private constructor() : LoggingHelper {

    companion object {

        @Volatile
        private var instance: LoggingHelperImpl? = null

        @Synchronized
        fun getInstance(): LoggingHelperImpl {
            return instance ?: LoggingHelperImpl().also { instance = it }
        }
    }

    override fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun debug(tag: String, message: String, t: Throwable) {
        Log.d(tag, message, t)
    }

    override fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun info(tag: String, message: String, t: Throwable) {
        Log.i(tag, message, t)
    }

    override fun warn(tag: String, message: String) {
        Log.w(tag, message)
    }

    override fun warn(tag: String, message: String, t: Throwable) {
        Log.w(tag, message, t)
    }

    override fun error(tag: String, message: String) {
        Log.e(tag, message)
    }

    override fun error(tag: String, message: String, t: Throwable) {
        Log.e(tag, message, t)
    }
}
