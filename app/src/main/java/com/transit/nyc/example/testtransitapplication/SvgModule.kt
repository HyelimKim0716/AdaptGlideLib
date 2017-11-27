package com.transit.nyc.example.testtransitapplication

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import java.io.InputStream

/**
 * Created by Owner on 2017-11-27.
 */
class SvgModule : AppGlideModule() {
    override fun registerComponents(context: Context?, glide: Glide?, registry: Registry?) {
        registry?.register(SVG::class.java, PictureDrawable::class.java, SsvDrawableTranscoder())
                ?.append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}