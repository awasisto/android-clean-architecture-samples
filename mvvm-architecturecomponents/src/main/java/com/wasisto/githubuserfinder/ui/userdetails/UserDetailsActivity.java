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

import androidx.lifecycle.*;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.wasisto.githubuserfinder.R;
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl;
import com.wasisto.githubuserfinder.databinding.ActivityUserDetailsBinding;
import com.wasisto.githubuserfinder.domain.GetUserUseCase;
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl;

import static android.content.Intent.ACTION_VIEW;
import static android.widget.Toast.LENGTH_SHORT;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "username";

    private UserDetailsViewModel viewModel;

    private ActivityUserDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);

        UserDetailsViewModelFactory viewModelFactory =
                new UserDetailsViewModelFactory(
                        username,
                        new GetUserUseCase(GithubDataSourceImpl.getInstance(this)),
                        LoggingHelperImpl.getInstance()
                );

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_details);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        viewModel.getOpenBrowserEvent().observe(this, event -> {
            if (event != null) {
                Intent intent = new Intent(ACTION_VIEW);
                intent.setData(Uri.parse(event.getData()));

                startActivity(intent);
            }
        });

        viewModel.getShowToastEvent().observe(this, event -> {
            if (event != null) {
                Toast.makeText(this, event.getData(), LENGTH_SHORT).show();
            }
        });

        viewModel.getCloseActivityEvent().observe(this, event -> finish());
    }
}
