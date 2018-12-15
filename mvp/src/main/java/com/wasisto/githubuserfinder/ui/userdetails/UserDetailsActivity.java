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

package com.wasisto.githubuserfinder.ui.userdetails;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.domain.GetUserUseCase;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;

import static android.content.Intent.ACTION_VIEW;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;

public class UserDetailsActivity extends AppCompatActivity implements UserDetailsView {

    public static final String EXTRA_USERNAME = "username";

    private UserDetailsPresenter presenter;

    private ProgressBar loadingIndicator;

    private ImageView avatarImageView;

    private TextView nameTextView;

    private TextView usernameTextView;

    private TextView companyTextView;

    private TextView locationTextView;

    private TextView blogTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);

        presenter = new UserDetailsPresenterImpl(
                username,
                this,
                new GetUserUseCase(
                        GithubDataSourceImpl.getInstance(this)
                ),
                LoggingHelperImpl.getInstance()
        );

        loadingIndicator = findViewById(R.id.loadingIndicator);
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        companyTextView = findViewById(R.id.companyTextView);
        locationTextView = findViewById(R.id.locationTextView);
        blogTextView = findViewById(R.id.blogTextView);

        blogTextView.setOnClickListener(v -> presenter.onBlogClick());

        presenter.onViewReady();
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(GONE);
    }

    @Override
    public void showAvatar(String avatarUrl) {
        avatarImageView.setVisibility(VISIBLE);
        Picasso.get().load(avatarUrl).placeholder(R.color.colorAccent).into(avatarImageView);
    }

    @Override
    public void hideAvatar() {
        avatarImageView.setVisibility(GONE);
    }

    @Override
    public void showName(String name) {
        nameTextView.setVisibility(VISIBLE);
        nameTextView.setText(name);
    }

    @Override
    public void hideName() {
        nameTextView.setVisibility(GONE);
    }

    @Override
    public void showUsername(String username) {
        usernameTextView.setVisibility(VISIBLE);
        usernameTextView.setText(username);
    }

    @Override
    public void hideUsername() {
        usernameTextView.setVisibility(GONE);
    }

    @Override
    public void showCompany(String company) {
        companyTextView.setVisibility(VISIBLE);
        companyTextView.setText(company);
    }

    @Override
    public void hideCompany() {
        companyTextView.setVisibility(GONE);
    }

    @Override
    public void showLocation(String location) {
        locationTextView.setVisibility(VISIBLE);
        locationTextView.setText(location);
    }

    @Override
    public void hideLocation() {
        locationTextView.setVisibility(GONE);
    }

    @Override
    public void showBlog(String blog) {
        blogTextView.setVisibility(VISIBLE);
        blogTextView.setText(blog);
    }

    @Override
    public void hideBlog() {
        blogTextView.setVisibility(GONE);
    }

    @Override
    public void openBrowser(String uri) {
        Intent intent = new Intent(ACTION_VIEW);
        intent.setData(Uri.parse(uri));

        startActivity(intent);
    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        finish();
    }
}
