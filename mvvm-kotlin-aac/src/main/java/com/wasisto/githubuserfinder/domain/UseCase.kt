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

package com.wasisto.githubuserfinder.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wasisto.githubuserfinder.util.executor.ExecutorProvider

abstract class UseCase<in P, R> constructor(private val executorProvider: ExecutorProvider) {

    fun executeAsync(params: P): LiveData<Result<R>> {
        val result = MutableLiveData<Result<R>>()
        result.value = Result.Loading
        executorProvider.io().submit {
            try {
                result.postValue(Result.Success(execute(params)))
            } catch (error: Throwable) {
                result.postValue(Result.Error(error))
            }
        }
        return result
    }

    abstract fun execute(params: P): R

    sealed class Result<out T> {
        class Success<out T>(val data: T) : Result<T>()
        class Error(val error: Throwable) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }
}
