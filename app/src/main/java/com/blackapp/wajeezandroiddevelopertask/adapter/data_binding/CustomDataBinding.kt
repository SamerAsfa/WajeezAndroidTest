package com.blackapp.wajeezandroiddevelopertask.adapter.data_binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.blackapp.wajeezandroiddevelopertask.R
import com.bumptech.glide.Glide


@BindingAdapter("loadImage")
fun bindLoadImage(imageView: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrBlank()) {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    } else {
        // set default image
        Glide.with(imageView.context)
            .load(R.drawable.user)
            .into(imageView)

    }
}

@BindingAdapter("testViewWithoutImageProfileChecker")
fun testViewWithoutImageProfileChecker(textView: TextView, txt: String?) {
    if (txt.isNullOrBlank()) {
        textView.text = "Without Image"
    }
}