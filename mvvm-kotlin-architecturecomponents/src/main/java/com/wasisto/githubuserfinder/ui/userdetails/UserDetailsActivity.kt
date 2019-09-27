/*
 * Copyright (c) 2019 Andika Wasisto
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

package com.wasisto.githubuserfinder.ui.userdetails

import androidx.lifecycle.*
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl
import com.wasisto.githubuserfinder.databinding.ActivityUserDetailsBinding

import com.wasisto.githubuserfinder.domain.GetUserUseCase
import com.wasisto.githubuserfinder.util.executor.ExecutorProviderImpl
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl

class UserDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "username"
    }

    private lateinit var viewModel: UserDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username == null) {
            finish()
        } else {
            val viewModelFactory =
                UserDetailsViewModelFactory(
                    username,
                    GetUserUseCase(
                        ExecutorProviderImpl,
                        GithubDataSourceImpl.getInstance(this)
                    ),
                    LoggingHelperImpl
                )

            viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailsViewModel::class.java)

            DataBindingUtil.setContentView<ActivityUserDetailsBinding>(this, R.layout.activity_user_details).apply {
                lifecycleOwner = this@UserDetailsActivity
                viewModel = this@UserDetailsActivity.viewModel
            }

            viewModel.openBrowserEvent.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)

                    startActivity(intent)
                }
            })

            viewModel.showToastEvent.observe(this, Observer { event ->
                event.getContentIfNotHandled()?.let { resId ->
                    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.closeActivityEvent.observe(this, Observer { event ->
                if (!event.hasBeenHandled) {
                    finish()
                }
            })
        }
    }
}
