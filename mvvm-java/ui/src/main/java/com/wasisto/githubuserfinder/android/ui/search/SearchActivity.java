package com.wasisto.githubuserfinder.android.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.wasisto.githubuserfinder.android.DefaultServiceLocator;
import com.wasisto.githubuserfinder.android.R;
import com.wasisto.githubuserfinder.android.databinding.ActivitySearchBinding;
import com.wasisto.githubuserfinder.android.ui.common.ViewModelFactory;
import com.wasisto.githubuserfinder.android.ui.userdetails.UserDetailsActivity;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelFactory viewModelFactory = new ViewModelFactory(DefaultServiceLocator.getInstance(getApplication()));
        viewModel = viewModelFactory.create(SearchViewModel.class);

        ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        setContentView(binding.getRoot());

        viewModel.getOpenUserDetailsActivityEvent().observe(this, event -> {
            String username = event.getContentIfNotHandled();
            if (username != null) {
                startActivity(
                        new Intent(this, UserDetailsActivity.class) {{
                            putExtra(UserDetailsActivity.EXTRA_USERNAME, username);
                        }}
                );
            }
        });

        viewModel.getShowErrorToastEvent().observe(this, event -> {
            if (!event.hasBeenHandled()) {
                Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.load();
    }
}