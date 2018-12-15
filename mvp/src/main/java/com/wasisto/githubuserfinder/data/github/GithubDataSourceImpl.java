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

package com.wasisto.githubuserfinder.data.github;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wasisto.githubuserfinder.Callback;
import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.github.model.User;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class GithubDataSourceImpl implements GithubDataSource {

    private static final long CACHE_SIZE_BYTES = 1024;

    private static final long CONNECT_TIMEOUT_SECONDS = 10;

    private static final String BASE_URL = "https://api.github.com/";

    private static volatile GithubDataSourceImpl instance;

    private GithubService githubService;

    private GithubDataSourceImpl(Context context) {
        Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE_BYTES);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, SECONDS)
                .cache(cache)
                .build();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        githubService = retrofit.create(GithubService.class);
    }

    public static synchronized GithubDataSourceImpl getInstance(Context context) {
        if (instance == null) {
            instance = new GithubDataSourceImpl(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void searchUser(String query, Callback<SearchUserResult> callback) {
        githubService.searchUser(query).enqueue(new retrofit2.Callback<SearchUserResult>() {
            @Override
            public void onResponse(@NonNull Call<SearchUserResult> call,
                                   @NonNull Response<SearchUserResult> response) {
                try {
                    SearchUserResult searchUserResult = response.body();
                    callback.onSuccess(searchUserResult);
                } catch (Throwable t) {
                    callback.onError(t);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchUserResult> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }

    @Override
    public void getUser(String username, Callback<User> callback) {
        githubService.getUser(username).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                try {
                    User user = response.body();
                    callback.onSuccess(user);
                } catch (Throwable t) {
                    callback.onError(t);
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }
}
