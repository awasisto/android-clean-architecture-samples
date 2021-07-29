package com.wasisto.githubuserfinder.domain;

import com.wasisto.githubuserfinder.common.Logger;

public class NoOpLogger implements Logger {

    @Override
    public void trace(String message) {}

    @Override
    public void trace(String message, Throwable throwable) {}

    @Override
    public void debug(String message) {}

    @Override
    public void debug(String message, Throwable throwable) {}

    @Override
    public void info(String message) {}

    @Override
    public void info(String message, Throwable throwable) {}

    @Override
    public void warn(String message) {}

    @Override
    public void warn(String message, Throwable throwable) {}

    @Override
    public void error(String message) {}

    @Override
    public void error(String message, Throwable throwable) {}

    @Override
    public void fatal(String message) {}

    @Override
    public void fatal(String message, Throwable throwable) {}
}
