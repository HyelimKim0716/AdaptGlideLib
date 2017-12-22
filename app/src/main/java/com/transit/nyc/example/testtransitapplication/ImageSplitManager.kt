package com.transit.nyc.example.testtransitapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.util.Log

/**
 * Created by Owner on 2017-12-19.
 */
class ImageSplitManager(val context: Context) {
    val TAG = "ImageSplitManager"
    val LDPI = 0.75F
    val MDPI = 1F
    val HDPI = 1.5F
    val XHDPI = 2F
    val XXHDPI = 3F
    val XXXHDPI = 4F

    val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    val mColumnCount = getColumnCount()


    fun splitImage() : ArrayList<Bitmap> {
        val splitImages = arrayListOf<Bitmap>()

        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inJustDecodeBounds = true
        val originalBitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.map, options)

        Log.e("ImageSplitManager", "split, original image width : ${originalBitmap.width}, height : ${originalBitmap.height}, options width : ${options.outWidth}, height : ${options.outHeight}")
        val length = options.outWidth.div(mColumnCount)
        val rowCount = options.outHeight.div(length)

        Log.d(TAG, "splitImage, length : $length")

        for (y in 0 .. rowCount.minus(1)) {
            Log.d(TAG, "y = $y")
            for (x in 0 .. mColumnCount.minus(1)) {
                Log.d(TAG, "x = $x, x: ${length.times(x)}, y: ${length.times(y)}, " +
                        "width: ${length.times(x.plus(1))}, height: ${length.times(y.plus(1))}")
                splitImages.add(Bitmap.createBitmap(originalBitmap, length.times(x), length.times(y), length.times(x.plus(1)), length.times(y.plus(1))))
            }
        }

        options.inJustDecodeBounds = false

        return splitImages
    }

    fun getColumnCount(): Int = when(displayMetrics.density) {
        LDPI, MDPI, HDPI -> 6
        XHDPI -> 9
        XXHDPI, XXXHDPI -> 12
        else -> 9
    }



}