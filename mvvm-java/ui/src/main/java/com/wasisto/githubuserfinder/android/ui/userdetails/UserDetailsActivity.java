package com.wasisto.githubuserfinder.android.ui.userdetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.wasisto.githubuserfinder.android.DefaultServiceLocator;
import com.wasisto.githubuserfinder.android.R;
import com.wasisto.githubuserfinder.android.databinding.ActivityUserDetailsBinding;
import com.wasisto.githubuserfinder.android.ui.common.ViewModelFactory;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "username";

    private UserDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);

        if (username == null) {
            finish();
            return;
        }

        ViewModelFactory viewModelFactory = new ViewModelFactory(DefaultServiceLocator.getInstance(getApplication()));
        viewModel = viewModelFactory.create(UserDetailsViewModel.class);

        ActivityUserDetailsBinding binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        viewModel.getOpenBrowserEvent().observe(this, event -> {
            String url = event.getContentIfNotHandled();
            if (url != null) {
                startActivity(
                        new Intent(Intent.ACTION_VIEW) {{
                            setData(url.matches("(http|https)://") ? Uri.parse(url) : Uri.parse("http://" + url));
                        }}
                );
            }
        });

        viewModel.getShowErrorToastEvent().observe(this, event -> {
            if (!event.hasBeenHandled()) {
                Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getCloseActivityEvent().observe(this, event -> {
            finish();
        });

        viewModel.load(username);
    }
}