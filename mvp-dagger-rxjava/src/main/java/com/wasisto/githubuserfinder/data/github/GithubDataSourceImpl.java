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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wasisto.githubuserfinder.model.SearchUserResult;
import com.wasisto.githubuserfinder.model.User;
import io.reactivex.Observable;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Singleton
public class GithubDataSourceImpl implements GithubDataSource {

    private static final long CACHE_SIZE_BYTES = 1024;

    private static final long CONNECT_TIMEOUT_SECONDS = 10;

    private static final String BASE_URL = "https://api.github.com/";

    private GithubService githubService;

    @Inject
    public GithubDataSourceImpl(Context context) {
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        githubService = retrofit.create(GithubService.class);
    }

    @Override
    public Observable<SearchUserResult> searchUser(String query) {
        return githubService.searchUser(query);
    }

    @Override
    public Observable<User> getUser(String username) {
        return githubService.getUser(username);
    }
}
