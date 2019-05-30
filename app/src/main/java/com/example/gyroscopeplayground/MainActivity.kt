package com.example.gyroscopeplayground

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import kotlinx.android.synthetic.main.activity_main.cardView as mCard

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initGyroscopeSensor()

        mCard.setOnClickListener {
            //startElevationAnimation(mCard)
            //startFlipAnimation(mCard)
            //startFlipAnimation2(mCard)
        }

    }

    /* rotation em X = -1 -> bottom do card diminui
     rotation em X = 1 -> top do card diminui
     rotation em Y = -1 -> esquerda diminui
     rotation em Y = 1 -> direita diminui */
    private fun startFlipAnimation2(view: View, x: Float = 0f, y: Float = 0f) {
        view.animate()
                .rotationX(x)
                .rotationY(y)
                .setDuration(300)
                .start()
    }

    private fun startElevationAnimation(view: View) {
        view.animate()
                .translationZ(view.z * 2)
                .translationX((0..10).random().toFloat())
                .translationY((0..10).random().toFloat())
                .setDuration(350)
                .setInterpolator(FastOutLinearInInterpolator())
                .start()
    }

    private fun startFlipAnimation(view: View) {
        val oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                oa2.start()
            }
        })
        oa1.start()
    }

    //region Gyroscope impl
    private val gyroscopeSensorListener = (object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
            startFlipAnimation2(mCard, event.values[0], event.values[1])
        }
    })

    private fun initGyroscopeSensor() {
        val sensorManager = getSensorManager()
        sensorManager.registerListener(
                gyroscopeSensorListener,
                getGyroscopeSensor(sensorManager),
                SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun getSensorManager(): SensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private fun getGyroscopeSensor(sensorManager: SensorManager) =
            sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    //endregion

}
