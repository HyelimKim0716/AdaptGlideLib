package com.transit.nyc.example.testtransitapplication

import android.graphics.drawable.PictureDrawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target

/**
 * Created by Owner on 2017-11-27.
 */
class SvgSoftwareLayerSetter : RequestListener<PictureDrawable> {
    override fun onResourceReady(resource: PictureDrawable?, model: Any?, target: Target<PictureDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        val view: ImageView = ((target as ImageViewTarget<PictureDrawable>).view)
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        Log.d("Test", "onResourceReady")
        return false
    }

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<PictureDrawable>?, isFirstResource: Boolean): Boolean {
        val view: ImageView = ((target as ImageViewTarget<PictureDrawable>).view)
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null)
        Log.d("Test", "onLoadFailed")
        return false
    }
}