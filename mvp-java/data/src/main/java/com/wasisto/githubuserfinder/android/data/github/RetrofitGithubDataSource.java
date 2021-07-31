package com.wasisto.githubuserfinder.android.data.github;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.wasisto.githubuserfinder.domain.interfaces.GithubDataSource;
import com.wasisto.githubuserfinder.domain.models.SearchUsersResult;
import com.wasisto.githubuserfinder.domain.models.User;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGithubDataSource implements GithubDataSource {

    private static final String BASE_URL = "https://api.github.com/";

    private GithubService githubService = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                    GsonConverterFactory.create(
                            new GsonBuilder()
                                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .create()
                    )
            )
            .build()
            .create(GithubService.class);

    @Override
    public SearchUsersResult searchUsers(String query) throws Exception {
        Response<SearchUsersResult> response = githubService.searchUsers(query).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new Exception(response.message());
        }
    }

    @Override
    public User getUser(String username) throws Exception {
        Response<User> response = githubService.getUser(username).execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new Exception(response.message());
        }
    }
}
