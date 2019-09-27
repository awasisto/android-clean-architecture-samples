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

package com.wasisto.githubuserfinder.data.github

import android.content.Context
import com.google.gson.FieldNamingPolicy

import com.google.gson.GsonBuilder

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.wasisto.githubuserfinder.model.SearchUserResult
import com.wasisto.githubuserfinder.model.User
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

class GithubDataSourceImpl private constructor(context: Context) : GithubDataSource {

    companion object {

        private const val CACHE_SIZE_BYTES: Long = 1024

        private const val CONNECT_TIMEOUT_SECONDS: Long = 10

        private const val BASE_URL = "https://api.github.com/"

        @Volatile
        private var instance: GithubDataSourceImpl? = null

        @Synchronized
        fun getInstance(context: Context): GithubDataSourceImpl {
            return instance ?: GithubDataSourceImpl(context).also { instance = it }
        }
    }

    private val githubService: GithubService

    init {
        val cache = Cache(context.cacheDir, CACHE_SIZE_BYTES)

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .cache(cache)
                .build()

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        githubService = retrofit.create(GithubService::class.java)
    }

    override fun searchUser(query: String): SearchUserResult {
        val response = githubService.searchUser(query).execute()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw RuntimeException(response.message())
        }
    }

    override fun getUser(username: String): User {
        val response = githubService.getUser(username).execute()
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw RuntimeException(response.message())
        }
    }
}
