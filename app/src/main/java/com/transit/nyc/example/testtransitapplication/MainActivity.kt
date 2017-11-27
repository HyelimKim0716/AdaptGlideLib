package com.transit.nyc.example.testtransitapplication

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.PictureDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

import com.google.transit.realtime.GtfsRealtime
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class MainActivity : AppCompatActivity() {

    private val scaleGestureDetector by lazy {
        ScaleGestureDetector(applicationContext, ScaleListener())
    }

    private val requestBuilder: RequestBuilder<PictureDrawable> by lazy {

        Glide.with(applicationContext)
                .`as`(PictureDrawable::class.java)
                .listener(SvgSoftwareLayerSetter())

//                .load(R.drawable.nyc_subway_latest_map)
//                .into(iv_subway)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//        getMsg()
//        getImageView()
        requestBuilder.load(R.drawable.nyc_subway_latest_map)

                .into(iv_subway)

    }

    private fun getMsg(period: Int) {
        Observable.interval(3, TimeUnit.SECONDS)
                .subscribe({
                    Thread(Runnable {
                        try {
                            val url = URL("http://datamine.mta.info/mta_esi.php?key=5cf46badeadf2c4a9099390db1a387c6&feed_id=21")
                            val feedMessage = GtfsRealtime.FeedMessage.parseFrom(url.openStream())

                            val stringBuffer = StringBuffer()

                            feedMessage.entityList.forEach {
                                stringBuffer.append(it.tripUpdate.toString() + "\n")
                            }

//                            runOnUiThread { tv_entity.text = stringBuffer.toString() }
                        } catch (e: MalformedURLException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }).start()
                }, {
//                    tv_entity.text = it.message
                })
    }

    private fun getMsg() {
        Thread(Runnable {
            try {
                val url = URL("http://datamine.mta.info/mta_esi.php?key=5cf46badeadf2c4a9099390db1a387c6&feed_id=21")
                val feedMessage = GtfsRealtime.FeedMessage.parseFrom(url.openStream())

                val stringBuffer = StringBuffer()

                feedMessage.entityList.forEach {
                    stringBuffer.append(it.tripUpdate.toString() + "\n")
                }

                println(stringBuffer.toString())
//                            runOnUiThread { tv_entity.text = stringBuffer.toString() }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    fun getImageView() {
        Glide.with(applicationContext)
                .load(R.drawable.jpg_nyc_subway_map_north)
                .into(iv_subway)

//        iv_subway.setOnClickListener {
//         val values = intArrayOf(0, 1)
//            it.getLocationOnScreen(values)
//            println("X = ${values[0]}, Y = ${values[1]}")
//
//        }
        val matrix = Matrix()
        val scale = 1F
        val scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(applicationContext, object: ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        println("X = ${iv_subway.x} Y = ${iv_subway.y}")
    }

    fun getLocationOnScreen(view: View) : Rect {
        val rect = Rect()
        val location = intArrayOf(0, 1)

        view.getLocationOnScreen(location)
        rect.left = location[0]
        rect.top = location[1]
        rect.right = location[1] + view.width
        rect.bottom = location[1] + view.height

//        println("left = ${rect.left}, top = ${rect.top}, right = ${rect.right}, bottom = ${rect.bottom}")
        return rect
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchedX = event?.x?.toInt()
        val touchedY = event?.y?.toInt()

//        println("Action = " + event?.action)
        val image = getLocationOnScreen(iv_subway)
        if (event?.action == MotionEvent.ACTION_DOWN
                && image.contains(touchedX ?: 0, touchedY ?: 0)) {

//            println("X = $x Y = $y")
            println("Image X = $touchedX Y = ${touchedY?.minus(image.top)}")
            val imageX = touchedX
            val imageY = touchedY?.minus(image.top) ?: 0

            // Way 1
//            val lon = x?.times(360)?.div(image.width())?.minus(180) ?: 0
//            val lat = y?.times(180)?.div(image.height())?.plus(90) ?: 0

            // Way 2
            // bottom X = -73.747606, top X = -73.038205
            // bottom Y = 40.564429, top Y = 40.913577
//            val degreesPerPixelX = (-73.747606).minus((-73.038205).div(imageX!!))
//            val degreesPerPixelY = (40.564429).minus((40.913577).div(imageY!!))
//            val lon = (-73.038205).plus(touchedX.times(degreesPerPixelX))
//            val lat = (40.564429).plus(touchedY?.times(degreesPerPixelY)!!)



            // Way3
            // van park x = 1375, y = 284
//            val left = GeoPoint(40.913577, -74.038205)
//            val degreesPerPixelX = calculateDistance(-73.747606, -73.038205).div(1375))
//            val degreesPerPixelY = (40.564429).minus((40.913577).div(284))
//
//            val lon = (-73.038205).plus(1375.times(degreesPerPixelX))
//            val lat = (40.564429).plus(284.times(degreesPerPixelY))

//            val point = Geopoi
//            Toast.makeText(applicationContext, "X = $imageX Y = $imageY, lat = $lat, lon = $lon", Toast.LENGTH_SHORT).show()

//            println("lat = $lat, lon = $lon")
        }

        scaleGestureDetector.onTouchEvent(event)
        return true
    }

//    fun calculateDistance(): Double {
//        val lat1 = startP.latitudeE6.div(1E6)
//        val lat2 = endP.latitudeE6.div(1E6)
//        val lon1 = startP.longitudeE6.div(1E6)
//        val lon2 = endP.longitudeE6.div(1E6)

//        val dLat = Math.toRadians(lat2.minus(lat1))
//        val dLon = Math.toRadians(lon2.minus(lon2))

//         double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                Math.sin(dLng/2) * Math.sin(dLng/2);

//        val a = (Math.sin(dLat.div(2)).times(Math.sin(dLat.div(2))))
//                .plus((Math.cos(Math.toRadians(lat1)).times(Math.cos(Math.toRadians(lat2))).times(Math.sin(dLon.div(2))).times(Math.sin(dLon.div(2)))))
//        val c = 2.times(Math.asin(Math.sqrt(a)))
//
//        val earthRadius = 3958.75
//        val distance = earthRadius.times(c)
//        return distance.times(1609)     // meter conversion = 1609
//
//    }

    val matrix = Matrix()
    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            var scaleFactor = detector?.scaleFactor
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor ?: Float.MAX_VALUE, 5.0f))
            matrix.setScale(scaleFactor, scaleFactor)
            iv_subway.imageMatrix = matrix

            return true
        }
    }
}
