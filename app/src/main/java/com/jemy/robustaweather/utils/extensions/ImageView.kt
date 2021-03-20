package com.jemy.robustaweather.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.jemy.robustaweather.R

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}

fun ImageView.loadAsBitmap(byteArray: ByteArray) {
    Glide.with(context)
        .asBitmap()
        .load(byteArray)
        .placeholder(R.drawable.ic_launcher_background)
        .into(this)
}