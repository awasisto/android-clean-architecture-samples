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

package com.wasisto.githubuserfinder.data.github

import android.content.Context

import com.google.gson.GsonBuilder
import com.wasisto.githubuserfinder.model.SearchUserResult
import com.wasisto.githubuserfinder.model.User

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES
import java.util.concurrent.TimeUnit.SECONDS

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
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, SECONDS)
            .cache(cache)
            .build()

        val gson = GsonBuilder()
            .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        githubService = retrofit.create(GithubService::class.java)
    }

    override fun searchUser(query: String, onSuccess: (SearchUserResult) -> Unit, onError: (Throwable) -> Unit) {
        githubService.searchUser(query).enqueue(object : retrofit2.Callback<SearchUserResult> {
            override fun onResponse(call: Call<SearchUserResult>, response: Response<SearchUserResult>) {
                try {
                    val searchUserResult = response.body()
                    onSuccess.invoke(searchUserResult!!)
                } catch (t: Throwable) {
                    onError.invoke(t)
                }
            }

            override fun onFailure(call: Call<SearchUserResult>, t: Throwable) {
                onError.invoke(t)
            }
        })
    }

    override fun getUser(username: String, onSuccess: (User) -> Unit, onError: (Throwable) -> Unit) {
        githubService.getUser(username).enqueue(object : retrofit2.Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                try {
                    val user = response.body()
                    onSuccess.invoke(user!!)
                } catch (t: Throwable) {
                    onError.invoke(t)
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onError.invoke(t)
            }
        })
    }
}
