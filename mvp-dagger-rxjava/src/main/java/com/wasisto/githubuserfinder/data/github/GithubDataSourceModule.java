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
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Module
public class GithubDataSourceModule {

    private static final long CACHE_SIZE_BYTES = 1024;

    private static final long CONNECT_TIMEOUT_SECONDS = 10;

    private static final String BASE_URL = "https://api.github.com/";

    @Singleton
    @Provides
    public Cache provideOkHttpCache(Context context) {
        return new Cache(context.getCacheDir(), CACHE_SIZE_BYTES);
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Cache cache) {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, SECONDS)
                .cache(cache)
                .build();
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Singleton
    @Provides
    public GithubService provideGithubService(Retrofit retrofit) {
        return retrofit.create(GithubService.class);
    }
}
