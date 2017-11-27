package com.transit.nyc.example.testtransitapplication

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import com.caverock.androidsvg.SVGParseException
import java.io.IOException
import java.io.InputStream

/**
 * Created by Owner on 2017-11-27.
 */
class SvgDecoder : ResourceDecoder<InputStream, SVG> {
    override fun handles(source: InputStream?, options: Options?): Boolean {
        return true
    }

    override fun decode(source: InputStream?, width: Int, height: Int, options: Options?): Resource<SVG>? {
        try {
            val svg: SVG = SVG.getFromInputStream(source)
            return SimpleResource<SVG>(svg)
        } catch (ex: SVGParseException) {
            throw IOException("Cannot load SVG from stream", ex)
        }
    }

}