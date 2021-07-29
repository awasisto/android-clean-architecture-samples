package com.wasisto.githubuserfinder.android.data.github;

import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

interface GithubService {

    @GET("search/users")
    Call<SearchUsersResult> searchUsers(@Query("q") String query);

    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);
}
