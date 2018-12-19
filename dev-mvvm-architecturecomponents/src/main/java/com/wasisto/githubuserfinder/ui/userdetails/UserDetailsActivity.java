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

import android.arch.lifecycle.*;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.data.github.model.User;
import com.wasisto.githubuserfinder.domain.GetUserUseCase;
import com.wasisto.githubuserfinder.util.logging.LoggingHelper;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;

import static android.content.Intent.ACTION_VIEW;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static com.wasisto.githubuserfinder.data.Resource.Status.ERROR;
import static com.wasisto.githubuserfinder.data.Resource.Status.LOADING;
import static com.wasisto.githubuserfinder.data.Resource.Status.SUCCESS;

public class UserDetailsActivity extends AppCompatActivity {

    private static final String TAG = "UserDetailsActivity";

    public static final String EXTRA_USERNAME = "username";

    private UserDetailsViewModel viewModel;

    private ProgressBar loadingIndicator;

    private ImageView avatarImageView;

    private TextView nameTextView;

    private TextView usernameTextView;

    private TextView companyTextView;

    private TextView locationTextView;

    private TextView blogTextView;

    private LoggingHelper loggingHelper = LoggingHelperImpl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);

        viewModel =
                ViewModelProviders.of(
                        this,
                        new ViewModelProvider.Factory() {
                            @NonNull
                            @Override
                            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                                // noinspection unchecked
                                return (T) new UserDetailsViewModel(
                                        username,
                                        new GetUserUseCase(
                                                GithubDataSourceImpl.getInstance(UserDetailsActivity.this)
                                        )
                                );
                            }
                        }
                ).get(UserDetailsViewModel.class);

        loadingIndicator = findViewById(R.id.loadingIndicator);
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        companyTextView = findViewById(R.id.companyTextView);
        locationTextView = findViewById(R.id.locationTextView);
        blogTextView = findViewById(R.id.blogTextView);


        viewModel.getUser().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == LOADING) {
                    loadingIndicator.setVisibility(VISIBLE);

                    avatarImageView.setVisibility(GONE);
                    nameTextView.setVisibility(GONE);
                    usernameTextView.setVisibility(GONE);
                    companyTextView.setVisibility(GONE);
                    locationTextView.setVisibility(GONE);
                    blogTextView.setVisibility(GONE);
                } else {
                    loadingIndicator.setVisibility(GONE);

                    avatarImageView.setVisibility(VISIBLE);
                    nameTextView.setVisibility(VISIBLE);
                    usernameTextView.setVisibility(VISIBLE);
                    companyTextView.setVisibility(VISIBLE);
                    locationTextView.setVisibility(VISIBLE);
                    blogTextView.setVisibility(VISIBLE);

                    if (resource.status == SUCCESS) {
                        User user = resource.data;

                        Picasso.get().load(user.getAvatarUrl()).placeholder(R.color.colorAccent).into(avatarImageView);
                        nameTextView.setText(user.getName());
                        usernameTextView.setText(user.getLogin());
                        companyTextView.setText(user.getCompany());
                        locationTextView.setText(user.getLocation());
                        blogTextView.setText(user.getBlog());

                        blogTextView.setOnClickListener(v -> {
                            Intent intent = new Intent(ACTION_VIEW);
                            intent.setData(Uri.parse(user.getBlog()));

                            startActivity(intent);
                        });
                    } else if (resource.status == ERROR) {
                        loggingHelper.error(TAG, "An error occurred while getting a user", resource.error);

                        Toast.makeText(this, R.string.an_error_occurred, LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}
