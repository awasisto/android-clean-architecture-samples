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

import com.wasisto.githubuserfinder.util.executor.ExecutorProvider;

public abstract class UseCase<P, R> {

    private ExecutorProvider executorProvider;

    public UseCase(ExecutorProvider executorProvider) {
        this.executorProvider = executorProvider;
    }

    public void executeAsync(P params, Callback<R> callback) {
        executorProvider.io().submit(() -> {
            try {
                R result = execute(params);
                executorProvider.ui().submit(() -> callback.onSuccess(result));
            } catch (Throwable error) {
                executorProvider.ui().submit(() -> callback.onError(error));
            }
        });
    }

    abstract R execute(P params) throws Throwable;

    public interface Callback<T> {

        void onSuccess(T result);

        void onError(Throwable error);
    }
}
