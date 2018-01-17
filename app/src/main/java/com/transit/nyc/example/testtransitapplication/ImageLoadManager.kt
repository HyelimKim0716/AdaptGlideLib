package com.transit.nyc.example.testtransitapplication

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by Owner on 2017-12-12.
 */
class ImageLoadManager(val activity: Activity, val resources: Resources) {
    private val LDPI = 0.75F
    private val MDPI = 1F
    private val HDPI = 1.5F
    private val XHDPI = 2F
    private val XXHDPI = 3F
    private val XXXHDPI = 4F

    //    "file:///android_asset/new_york_map/tiles_xres/"
    private val HIGH_RES_PATH = "prev_new_york_map/tiles_high_res"
    private val X_RES_PATH = "prev_new_york_map/tiles_xres"
    private val XX_RES_PATH = "prev_new_york_map/tiles_xxres"

    private val displayMetrics: DisplayMetrics = resources.displayMetrics

    private var images = getResources()
    private val mColumnCount = getColumCount()
    private val mRowCount = images.size.div(mColumnCount)

    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    fun getCombinedImageOriginalImageSizeMinus1(): Bitmap? {
        var bitmap = getOriginalImageSizeMinus1Bitmap()
        val canvas = Canvas(bitmap)

        mBitmapWidth = bitmap.width
        mBitmapHeight = bitmap.height
        println("bitmap size = $mBitmapWidth $mBitmapHeight / image size = ${images.size}")

        images.forEachIndexed { index, one ->
            canvas.drawBitmap(one, null, getDst(index, one), null)
        }

        return bitmap
    }

    fun getCombinedImageOriginalImageSizeMinus2(): Bitmap? {
        var bitmap = getFixedSizeBitmap4254()
        val canvas = Canvas(bitmap)

        mBitmapWidth = bitmap.width
        mBitmapHeight = bitmap.height
        println("bitmap size = $mBitmapWidth $mBitmapHeight / image size = ${images.size}")

        images.forEachIndexed { index, one ->
            canvas.drawBitmap(one, null, getDst(index, one), null)
        }

        return bitmap
    }

    fun getFixedSizeBitmap4254() = Bitmap.createBitmap(4200, 5400, Bitmap.Config.ARGB_8888)

    fun get3TimesImageSizeBitmap() = Bitmap.createBitmap(images[0].width.times(3), images[0].height.times(3), Bitmap.Config.ARGB_8888)

    fun getScreenSizeBitmap() = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.Config.ARGB_8888)

    fun getOriginalImageSizeBitmap() = Bitmap.createBitmap(images[0].width.times(mColumnCount), images[0].height.times(mRowCount), Bitmap.Config.ARGB_8888)

    fun getOriginalImageWidthSizeBitmap() = Bitmap.createBitmap(images[0].width.times(mColumnCount), images[0].height.times(mRowCount.minus(3)), Bitmap.Config.ARGB_8888)

    fun getOriginalImageSizeMinus1Bitmap()
            = Bitmap.createBitmap(
            images[0].width.times(mColumnCount.minus(1)),
            images[0].height.times(mRowCount.minus(1)),
            Bitmap.Config.ARGB_8888)

    fun getOriginalImageSizeMinus2Bitmap()
            = Bitmap.createBitmap(
            images[0].width.times(mColumnCount.minus(2)),
            images[0].height.times(mRowCount.minus(2)),
            Bitmap.Config.ARGB_8888)
    
    fun getScreenWidthSizeBitmap(): Bitmap {
        val display = (activity.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        println("screen width : ${size.x} screen height : ${size.y}")
        return Bitmap.createBitmap(size.x, size.x, Bitmap.Config.ARGB_4444)
    }


    fun getColumCount(): Int
            = when (displayMetrics.density) {   // 6, 9, 12
        LDPI, MDPI, HDPI -> 7
        XHDPI -> 9
        XXHDPI, XXXHDPI  -> 12
        else -> 7
    }

    fun getResources(): ArrayList<Bitmap> {
        val imageRes: ArrayList<Bitmap> = ArrayList()

        println("density : ${displayMetrics.density}, densityDpi : ${displayMetrics.densityDpi}")

        var imagePath: String? = null
        when (displayMetrics.density) {
            LDPI, MDPI, HDPI -> {
                imagePath = HIGH_RES_PATH

//                (0..activity.assets.list("prev_new_york_map/tiles_high_res").size.minus(1)).mapTo(imageRes) { BitmapFactory.decodeStream(activity.assets.open("$HIGH_RES_PATH${String.format("nyc_500_%03d.png", it)}")) }
            }
            XHDPI -> imagePath = X_RES_PATH
            XXHDPI, XXXHDPI -> imagePath = XX_RES_PATH
        }

        println("list size : " + resources.assets.list(imagePath).size)

//       for (x in 0..activity.assets.list("prev_new_york_map/tiles_xres").size.minus(1)) {
//           imageRes.add(BitmapFactory.decodeStream(activity.assets.open("$X_RES_PATH${String.format("nyc_500_%03d.png", x)}")))
//       }

//        activity.assets.list(imagePath).forEach {
//            println("it = $it")
//            imageRes.add(BitmapFactory.decodeStream(activity.assets.open("$imagePath/$it")))
//        }

        activity.assets.list(imagePath).mapTo(imageRes, {
            println("it = $it")
            BitmapFactory.decodeStream(activity.assets.open("$imagePath/$it"))
        })
        return imageRes
    }

    fun getDst(index: Int, bitmap: Bitmap): Rect {
        val width = mBitmapWidth.div(mColumnCount)
        val height = mBitmapHeight.div(mRowCount)

        // Original Image Size
//        val width = bitmap.width
//        val height = bitmap.height

        // Screen Size
//        val width = displayMetrics.widthPixels.div(mColumnCount)
//        val height = displayMetrics.heightPixels.div(mRowCount)

        println("index : $index mColumCount : $mColumnCount mRowCount : $mRowCount / left: ${width.times(index% mColumnCount)} top: ${height.times(index.div(mColumnCount))} right: ${width.times((index%mColumnCount) + 1)} bottom: ${height.times(index.div(mColumnCount).plus(1))}")
        val rect = Rect(width.times(index% mColumnCount), height.times(index.div(mColumnCount)), width.times((index%mColumnCount) + 1), height.times(index.div(mColumnCount).plus(1)))
        return rect
    }
}