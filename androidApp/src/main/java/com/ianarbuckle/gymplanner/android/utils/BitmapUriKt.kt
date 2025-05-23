package com.ianarbuckle.gymplanner.android.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun ImageBitmap.imageBitmapToUri(context: Context, fileName: String): Uri? {
    val bitmap = asAndroidBitmap()
    return saveBitmapToFile(context, bitmap, fileName)
}

private fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): Uri? {
    val file = File(context.cacheDir, fileName)
    return try {
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, ImageQuality, outputStream)
        outputStream.flush()
        outputStream.close()
        Uri.fromFile(file)
    } catch (e: IOException) {
        Log.e("BitmapUriKt", "Error saving bitmap to file", e)
        null
    }
}

private const val ImageQuality = 100
