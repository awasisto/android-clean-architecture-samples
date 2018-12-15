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

package com.wasisto.githubuserfinder.ui.userdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.squareup.picasso.Picasso
import com.wasisto.githubuserfinder.R
import com.wasisto.githubuserfinder.data.github.GithubDataSourceImpl
import com.wasisto.githubuserfinder.util.logging.LoggingHelperImpl

import android.content.Intent.ACTION_VIEW
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast.LENGTH_SHORT
import com.wasisto.githubuserfinder.domain.GetUserUseCase

import kotlinx.android.synthetic.main.activity_user_details.*

class UserDetailsActivity : AppCompatActivity(), UserDetailsView {

    companion object {

        const val EXTRA_USERNAME = "username"
    }

    private lateinit var presenter: UserDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        presenter = UserDetailsPresenterImpl(
            username,
            this,
            GetUserUseCase(
                GithubDataSourceImpl.getInstance(this)
            ),
            LoggingHelperImpl.getInstance()
        )

        blogTextView.setOnClickListener { presenter.onBlogClick() }

        presenter.onViewReady()
    }

    override fun showLoadingIndicator() {
        loadingIndicator.visibility = VISIBLE
    }

    override fun hideLoadingIndicator() {
        loadingIndicator.visibility = GONE
    }

    override fun showAvatar(avatarUrl: String) {
        avatarImageView.visibility = VISIBLE
        Picasso.get().load(avatarUrl).placeholder(R.color.colorAccent).into(avatarImageView)
    }

    override fun hideAvatar() {
        avatarImageView.visibility = GONE
    }

    override fun showName(name: String) {
        nameTextView.visibility = VISIBLE
        nameTextView.text = name
    }

    override fun hideName() {
        nameTextView.visibility = GONE
    }

    override fun showUsername(username: String) {
        usernameTextView.visibility = VISIBLE
        usernameTextView.text = username
    }

    override fun hideUsername() {
        usernameTextView.visibility = GONE
    }

    override fun showCompany(company: String) {
        companyTextView.visibility = VISIBLE
        companyTextView.text = company
    }

    override fun hideCompany() {
        companyTextView.visibility = GONE
    }

    override fun showLocation(location: String) {
        locationTextView.visibility = VISIBLE
        locationTextView.text = location
    }

    override fun hideLocation() {
        locationTextView.visibility = GONE
    }

    override fun showBlog(blog: String) {
        blogTextView.visibility = VISIBLE
        blogTextView.text = blog
    }

    override fun hideBlog() {
        blogTextView.visibility = GONE
    }

    override fun openBrowser(uri: String) {
        val intent = Intent(ACTION_VIEW)
        intent.data = Uri.parse(uri)

        startActivity(intent)
    }

    override fun showToast(resId: Int) {
        Toast.makeText(this, resId, LENGTH_SHORT).show()
    }

    override fun closeActivity() {
        finish()
    }
}
