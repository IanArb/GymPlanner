package com.ianarbuckle.gymplanner.android.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.test.FakeImageLoaderEngine
import coil3.test.default

class FakeImageLoaderFactory(
    private val defaultDrawable: Drawable = ColorDrawable(Color.LTGRAY),
) : SingletonImageLoader.Factory {
    override fun newImageLoader(context: Context): ImageLoader = ImageLoader
        .Builder(context)
        .components { add(FakeImageLoaderEngine.Builder().default(defaultDrawable).build()) }
        .build()
}
