package com.wasisto.githubuserfinder.android.ui.userdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wasisto.githubuserfinder.android.DefaultServiceLocator;
import com.wasisto.githubuserfinder.android.R;
import com.wasisto.githubuserfinder.android.ServiceLocator;
import com.wasisto.githubuserfinder.domain.models.User;
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase;

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

        if (username == null) {
            finish();
            return;
        }

        ServiceLocator serviceLocator = DefaultServiceLocator.getInstance(getApplication());

        presenter = new UserDetailsPresenter(
                this,
                new GetUserUseCase(
                        serviceLocator.getGithubDataSource()
                ),
                serviceLocator.getLogger(UserDetailsPresenter.class),
                serviceLocator.getIoExecutorService(),
                serviceLocator.getMainExecutorService()
        );

        loadingIndicator = findViewById(R.id.loadingIndicator);
        avatarImageView = findViewById(R.id.avatarImageView);
        nameTextView = findViewById(R.id.nameTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        companyTextView = findViewById(R.id.companyTextView);
        locationTextView = findViewById(R.id.locationTextView);
        blogTextView = findViewById(R.id.blogTextView);

        blogTextView.setOnClickListener(v -> presenter.onBlogClick());

        presenter.load(username);
    }

    @Override
    public void showLoadingIndicator() {
        loadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        loadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public void showUser(User user) {
        Glide.with(this)
                .load(user.getAvatarUrl())
                .placeholder(R.color.teal_200)
                .into(avatarImageView);

        nameTextView.setText(user.getName());
        usernameTextView.setText(user.getLogin());
        companyTextView.setText(user.getCompany());
        locationTextView.setText(user.getLocation());
        blogTextView.setText(user.getBlog());
    }

    @Override
    public void openBrowser(String url) {
        startActivity(
                new Intent(Intent.ACTION_VIEW) {{
                    setData(url.matches("(http|https)://") ? Uri.parse(url) : Uri.parse("http://" + url));
                }}
        );
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeActivity() {
        finish();
    }
}