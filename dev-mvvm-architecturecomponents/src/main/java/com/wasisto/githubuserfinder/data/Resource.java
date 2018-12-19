package com.wasisto.githubuserfinder.data;

import static com.wasisto.githubuserfinder.data.Resource.Status.ERROR;
import static com.wasisto.githubuserfinder.data.Resource.Status.LOADING;
import static com.wasisto.githubuserfinder.data.Resource.Status.SUCCESS;

public class Resource<T> {

    public Status status;

    public T data;

    public Throwable error;

    private Resource(Status status, T data, Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable error) {
        return new Resource<>(ERROR, null, error);
    }

    public static <T> Resource<T> loading() {
        return new Resource<>(LOADING, null, null);
    }

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}