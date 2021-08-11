package com.wasisto.githubuserfinder.android.ui.userdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.wasisto.githubuserfinder.android.DefaultServiceLocator
import com.wasisto.githubuserfinder.android.R
import com.wasisto.githubuserfinder.domain.models.User
import com.wasisto.githubuserfinder.domain.usecases.GetUserUseCase
import kotlinx.android.synthetic.main.activity_user_details.*
import kotlinx.coroutines.Dispatchers

class UserDetailsActivity : AppCompatActivity(), UserDetailsView {

    companion object {
        const val EXTRA_USERNAME = "username"
    }

    private lateinit var presenter: UserDetailsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_details)

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username == null) {
            finish()
            return
        }

        val serviceLocator = DefaultServiceLocator.getInstance(application)

        presenter = UserDetailsPresenter(
            this,
            GetUserUseCase(
                serviceLocator.githubDataSource
            ),
            serviceLocator.getLogger(UserDetailsPresenter::class),
            serviceLocator.ioDispatcher,
            serviceLocator.mainDispatcher,
            lifecycleScope
        )

        blogTextView.setOnClickListener { presenter.onBlogClick() }

        presenter.load(username)
    }

    override fun showLoadingIndicator() {
        loadingIndicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        loadingIndicator.visibility = View.GONE
    }

    override fun showUser(user: User) {
        Glide.with(this)
            .load(user.avatarUrl)
            .placeholder(R.color.teal_200)
            .into(avatarImageView)

        nameTextView.text = user.name
        usernameTextView.text = user.login
        companyTextView.text = user.company
        locationTextView.text = user.location
        blogTextView.text = user.blog
    }

    override fun openBrowser(url: String) {
        startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                data = if (url.matches("(http|https)://".toRegex())) Uri.parse(url) else Uri.parse("http://$url")
            }
        )
    }

    override fun showErrorToast() {
        Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_SHORT).show()
    }

    override fun closeActivity() {
        finish()
    }
}