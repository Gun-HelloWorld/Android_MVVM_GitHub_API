package com.gun.githubapi.common.loader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class GlideAppModule : AppGlideModule()

@BindingAdapter("app:imageUrl", "app:placeholder")
fun loadImage(imageView: ImageView, url: String, placeholder: Drawable) {
    Glide.with(imageView.context)
        .load(url)
        .placeholder(placeholder)
        .sizeMultiplier(0.6f)
        .error(placeholder)
        .apply(RequestOptions().circleCrop())
        .skipMemoryCache(true)
        .into(imageView)
}
