package com.ianarbuckle.gymplanner.android.imageloading

import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.crossfade
import okio.Path.Companion.toOkioPath

class GymPlannerImageLoaderFactory : SingletonImageLoader.Factory {

    override fun newImageLoader(context: Context): ImageLoader =
        ImageLoader.Builder(context)
            .crossfade(true)
            .memoryCache {
                MemoryCache.Builder().maxSizePercent(context, MEMORY_CACHE_PERCENT).build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve(DISK_CACHE_DIRECTORY).toOkioPath())
                    .maxSizePercent(DISK_CACHE_PERCENT)
                    .build()
            }
            .build()

    private companion object {
        const val DISK_CACHE_DIRECTORY = "image_cache"
        const val MEMORY_CACHE_PERCENT = 0.25
        const val DISK_CACHE_PERCENT = 0.02
    }
}
