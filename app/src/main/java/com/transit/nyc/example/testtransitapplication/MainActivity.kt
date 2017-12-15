package com.transit.nyc.example.testtransitapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.OnMatrixChangedListener
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.OnSingleFlingListener

import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        iv_subway.setImageBitmap(resizeImage())
//        iv_subway.setImageBitmap(getBitmap())

        val imageManager = ImageManager(this, resources)


        val bitmap1: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.nyc_500_000)
        val bitmap2: Bitmap = BitmapFactory.decodeStream(assets.open("new_york_map/tiles_xres/${String.format("nyc_500_%03d.png", 1)}"))
        val bitmap = Bitmap.createBitmap(bitmap1.width.times(3), bitmap1.width.times(3), Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)
        println("bitmap size = ${bitmap.width} ${bitmap.height}")
//            images.forEachIndexed { index, bitmap ->
//                canvas.drawBitmap(bitmap, null, getDst(index, bitmap), null)
//            }
        var width = bitmap1.width
        var height = bitmap1.height
        println("width = $width, height = $height")
        var dst = Rect(0, 0, width , height)
        canvas.drawBitmap(bitmap1, null, dst, null)

        width = bitmap2.width
        height = bitmap2.height
        dst = Rect(bitmap1.width, 0, bitmap1.width + width, height)
        canvas.drawBitmap(bitmap2, null, dst, null)

//        Glide.with(this)
//                .load(bitmap)
//                .into(iv_subway)
//        iv_subway.setImageBitmap(bitmap)
        iv_subway.setImageBitmap(imageManager.getCombinedImage())
//        iv_subway.setImageBitmap(BitmapFactory.decodeStream(assets.open("new_york_map/tiles_xres/nyc_500_000.png")))
//        iv_subway.setImageBitmap(imageManager.getResources()[0])
        iv_subway.setOnMatrixChangeListener(mMatrixChangeListener)
        iv_subway.setOnPhotoTapListener(mPhotoTapListener)
        iv_subway.setOnSingleFlingListener(mSingleFlingListener)
    }

    private fun resizeImage() : Bitmap{
        val display = (applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val displayWidth = display.width
        val displayHeight = display.height

        // imageSize
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.nyc_subway_latest_map, options)
//        BitmapFactory.decodeResource(resources, R.drawable.nyc_subway_latest_map_250_270, options)


        val widthScale = options.outWidth.div(displayWidth)
        val heightScale = options.outHeight.div(displayHeight)
        val scale = if (widthScale > heightScale) widthScale else heightScale

        tv_image_size.text = "display width = $displayWidth, height = $displayHeight\n" +
                "Image width = ${options.outWidth} height = ${options.outHeight}\n" +
                "image scale = $widthScale, $heightScale, $scale"

        println("displayWid")
        if (scale >= 8) {
            options.inSampleSize = 8
        } else if (scale >= 6) {
            options.inSampleSize = 6
        } else if (scale >= 4) {
            options.inSampleSize = 4
        } else if (scale >= 2) {
            options.inSampleSize = 2
        } else {
            options.inSampleSize = 1
        }

        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(resources, R.drawable.nyc_subway_latest_map, options)
    }

//    fun getBitmap() : Bitmap {
//        val vector = ContextCompat.getDrawable(applicationContext, R.drawable.nyc_subway_latest_map)
//        val bitmap = Bitmap.createBitmap(vector.intrinsicWidth, vector.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        vector.setBounds(0, 0, canvas.width, canvas.height)
//        vector.draw(canvas)
//        return bitmap
//    }

    private val mPhotoTapListener = OnPhotoTapListener { view, x, y ->
        val xPercentage = x.times(100F)
        val yPercentage = y.times(100F)
        Toast.makeText(applicationContext, String.format("Photo Tap! X: %.2f %% Y:%.2f %% ID: %d", xPercentage, yPercentage, view?.id ?: 0), Toast.LENGTH_SHORT).show()
    }

    private val mMatrixChangeListener = OnMatrixChangedListener { rect ->
        tv_matrix.text = rect.toString()
    }

    private val mSingleFlingListener = OnSingleFlingListener { e1, e2, velocityX, velocityY ->
        val snackbar = Snackbar.make(window.decorView.rootView, "velocityX = $velocityX, velocityY = $velocityY", Snackbar.LENGTH_SHORT)
        snackbar.show()
        true
    }
}
