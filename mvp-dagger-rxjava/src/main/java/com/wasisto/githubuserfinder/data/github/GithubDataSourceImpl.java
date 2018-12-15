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

import com.wasisto.githubuserfinder.data.github.model.SearchUserResult;
import com.wasisto.githubuserfinder.data.github.model.User;
import io.reactivex.Observable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GithubDataSourceImpl implements GithubDataSource {

    private GithubService githubService;

    @Inject
    public GithubDataSourceImpl(GithubService githubService) {
        this.githubService = githubService;
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
