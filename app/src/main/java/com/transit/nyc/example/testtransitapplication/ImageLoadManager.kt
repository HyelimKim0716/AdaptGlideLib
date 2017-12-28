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
    val LDPI = 0.75F
    val MDPI = 1F
    val HDPI = 1.5F
    val XHDPI = 2F
    val XXHDPI = 3F
    val XXXHDPI = 4F

    val HIGH_RES_PATH = "prev_new_york_map/tiles_high_res/"
    val X_RES_PATH = "prev_new_york_map/tiles_xres/"
    val XX_RES_PATH = "prev_new_york_map/tiles_xxres/"
//    val X_RES_PATH      = "file:///android_asset/new_york_map/tiles_xres/"
//    val XX_RES_PATH     = "file:///android_asset/new_york_map/tiles_xxres/"

    val displayMetrics: DisplayMetrics = resources.displayMetrics

    var images = getResources()
    val mColumnCount = getColumCount()
    val mRowCount = images.size.div(mColumnCount)


    fun getCombinedImage(): Bitmap? {
        var bitmap: Bitmap? = null

        bitmap = getImageSizeBitmap()
        val canvas: Canvas = Canvas(bitmap)
        println("bitmap size = ${bitmap.width} ${bitmap.height} / image size = ${images.size}")
//            images.forEachIndexed { index, one ->
//                canvas.drawBitmap(one, null, getDst(index, one), null)
//            }

        for (x in 0..images.size.minus(1)) {
            canvas.drawBitmap(images[x], null, getDst(x, images[x]), null)
        }

        return bitmap
    }

    fun getFixedSizeBitmap3600(): Bitmap = Bitmap.createBitmap(3600, 3600, Bitmap.Config.ARGB_8888)

    fun get3TimesImageBitmap(): Bitmap = Bitmap.createBitmap(images[0].width.times(3), images[0].height.times(3), Bitmap.Config.ARGB_8888)

    fun getScreenSizeBitmap(): Bitmap = Bitmap.createBitmap(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.Config.ARGB_8888)

    fun getScreenWidthSizeBitmap(): Bitmap {
        val display = (activity.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        println("screen width : ${size.x} screen height : ${size.y}")
        return Bitmap.createBitmap(size.x, size.x, Bitmap.Config.ARGB_4444)
    }

    fun getImageSizeBitmap(): Bitmap {
        println("${images[0].width} ${images[0].height}")
        return Bitmap.createBitmap(images[0].width.times(mColumnCount), /*images[0].height.times(images.size.div(7))*/ images[0].height.times(3), Bitmap.Config.ARGB_8888)
    }

    fun getColumCount(): Int
    = when (displayMetrics.density) {   // 6, 9, 12
        LDPI, MDPI, HDPI -> 7
        XHDPI -> 9
        XXHDPI, XXXHDPI  -> 12
        else -> 7
    }

    fun getResources(): ArrayList<Bitmap> {

        var imageRes: ArrayList<Bitmap> = ArrayList()

        println("density : ${displayMetrics.density}, densityDpi : ${displayMetrics.densityDpi}")
        println("list size : " + resources.assets.list("prev_new_york_map/tiles_high_res").size)
        println("list size : " + resources.assets.list("prev_new_york_map/tiles_xres").size)
        println("list size : " + resources.assets.list("prev_new_york_map/tiles_xxres").size)



        when (displayMetrics.density) {
            LDPI, MDPI, HDPI -> {
                (0..activity.assets.list("prev_new_york_map/tiles_high_res").size.minus(1)).mapTo(imageRes) { BitmapFactory.decodeStream(activity.assets.open("$HIGH_RES_PATH${String.format("nyc_500_%03d.png", it)}")) }
            }
            XHDPI -> {
//                (0..activity.assets.list("new_york_map/tiles_xres").size.minus(1)).mapTo(imageRes) { BitmapFactory.decodeStream(activity.assets.open("$X_RES_PATH${String.format("nyc_500_%03d.png", it)}")) }
                for (x in 0..activity.assets.list("prev_new_york_map/tiles_xres").size.minus(1)) {
                    imageRes.add(BitmapFactory.decodeStream(activity.assets.open("$X_RES_PATH${String.format("nyc_500_%03d.png", x)}")))
                }
            }
            XXHDPI, XXXHDPI -> {
                (0..activity.assets.list("prev_new_york_map/tiles_xxres").size.minus(1)).mapTo(imageRes) {
                    println("it : $it")
                    BitmapFactory.decodeStream(activity.assets.open("$XX_RES_PATH${String.format("nyc_500_%03d.png", it)}")) }
            }
        }
        return imageRes

    }

    fun getDst(index: Int, bitmap: Bitmap): Rect {
//        val width = bitmap.width
//        val height = bitmap.height

        val width = displayMetrics.widthPixels.div(mColumnCount)
        val height = displayMetrics.heightPixels.div(mRowCount)

        println("index : $index mColumCount : $mColumnCount left: ${width.times(index% mColumnCount)} top: ${height.times(index.div(mColumnCount))} right: ${width.times((index%mColumnCount) + 1)} bottom: ${height.times(index.div(mColumnCount).plus(1))}")
        val rect = Rect(width.times(index% mColumnCount), height.times(index.div(mColumnCount)), width.times((index%mColumnCount) + 1), height.times(index.div(mColumnCount).plus(1)))
        return rect
    }
}