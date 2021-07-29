package com.wasisto.githubuserfinder.android.data.logger;

import android.util.Log;

import com.wasisto.githubuserfinder.common.Logger;

public class AndroidLogger implements Logger {

    private static final int MAX_TAG_LENGTH = 23;

    private String tag;

    public AndroidLogger(Class<?> cls) {
        this.tag = cls.getSimpleName().length() > MAX_TAG_LENGTH ? cls.getSimpleName().substring(0, MAX_TAG_LENGTH - 1) : cls.getSimpleName();
    }

    @Override
    public void trace(String message) {
        Log.v(tag, message);
    }

    @Override
    public void trace(String message, Throwable throwable) {
        Log.v(tag, message, throwable);
    }

    @Override
    public void debug(String message) {
        Log.d(tag, message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        Log.d(tag, message, throwable);
    }

    @Override
    public void info(String message) {
        Log.i(tag, message);
    }

    @Override
    public void info(String message, Throwable throwable) {
        Log.i(tag, message, throwable);
    }

    @Override
    public void warn(String message) {
        Log.w(tag, message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        Log.w(tag, message, throwable);
    }

    @Override
    public void error(String message) {
        Log.e(tag, message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        Log.e(tag, message, throwable);
    }

    @Override
    public void fatal(String message) {
        Log.wtf(tag, message);
    }

    @Override
    public void fatal(String message, Throwable throwable) {
        Log.wtf(tag, message, throwable);
    }
}
