/*
 * Copyright (c) 2019 Andika Wasisto
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

package com.wasisto.githubuserfinder.usecase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wasisto.githubuserfinder.util.executor.ExecutorProvider;

public abstract class UseCase<P, R> {

    private ExecutorProvider executorProvider;

    public UseCase(ExecutorProvider executorProvider) {
        this.executorProvider = executorProvider;
    }

    public LiveData<Result<R>> executeAsync(P params) {
        MutableLiveData<Result<R>> result = new MutableLiveData<>();
        result.setValue(Result.loading());
        executorProvider.io().submit(() -> {
            try {
                result.postValue(Result.success(execute(params)));
            } catch (Throwable error) {
                result.postValue(Result.error(error));
            }
        });
        return result;
    }

    abstract R execute(P params) throws Throwable;

    public static class Result<T> {

        public enum Status {
            SUCCESS,
            ERROR,
            LOADING
        }

        public Status status;

        public T data;

        public Throwable error;

        private Result(Status status, T data, Throwable error) {
            this.status = status;
            this.data = data;
            this.error = error;
        }

        public static <T> Result<T> success(T data) {
            return new Result<>(Status.SUCCESS, data, null);
        }

        public static <T> Result<T> error(Throwable error) {
            return new Result<>(Status.ERROR, null, error);
        }

        public static <T> Result<T> loading() {
            return new Result<>(Status.LOADING, null, null);
        }
    }
}
