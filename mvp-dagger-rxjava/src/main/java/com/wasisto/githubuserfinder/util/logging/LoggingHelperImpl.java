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

package com.wasisto.githubuserfinder.util.logging;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoggingHelperImpl implements LoggingHelper {

    @Inject
    public LoggingHelperImpl() {
    }

    @Override
    public void debug(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    public void debug(String tag, String message, Throwable t) {
        Log.d(tag, message, t);
    }

    @Override
    public void info(String tag, String message) {
        Log.i(tag, message);
    }

    @Override
    public void info(String tag, String message, Throwable t) {
        Log.i(tag, message, t);
    }

    @Override
    public void warn(String tag, String message) {
        Log.w(tag, message);
    }

    @Override
    public void warn(String tag, String message, Throwable t) {
        Log.w(tag, message, t);
    }

    @Override
    public void error(String tag, String message) {
        Log.e(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable t) {
        Log.e(tag, message, t);
    }
}
