package com.wasisto.githubuserfinder.android.ui.common;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.wasisto.githubuserfinder.android.R;

public class BindingAdapters {

    @BindingAdapter("goneUnless")
    public static void goneUnless(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("imageUrl")
    public static void imageUrl(ImageView imageView, String imageUrl) {
        Glide.with(imageView)
                .load(imageUrl)
                .placeholder(R.color.teal_200)
                .into(imageView);
    }
}
