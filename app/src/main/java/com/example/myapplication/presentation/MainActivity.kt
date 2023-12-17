/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.myapplication.presentation
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R


class MainActivity : Activity() {
    private var mSensorManager: SensorManager? = null
    private var mHeartSensor: Sensor? = null
    private var mTextView: TextView? = null
    private val mSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            if(event.values[0] > 0){
                println(event.values[0])
                mTextView?.text  = event.values[0].toString()
            }

        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Check for body sensor permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BODY_SENSORS)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request for permission
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.BODY_SENSORS),
                REQUEST_BODY_SENSOR_PERMISSION)
        }
        mTextView = findViewById<View>(R.id.text1) as TextView
        mTextView!!.text = "0"
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mHeartSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            REQUEST_BODY_SENSOR_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, continue with sensor registration
                } else {
                    // Permission denied, disable the functionality that depends on this permission.
                }
                return
            }
            // Add other 'when' lines to check for other permissions your app might request.
        }
    }
    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(
            mSensorEventListener,
            mHeartSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    //
    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(mSensorEventListener)
    }
    companion object {
        const val REQUEST_BODY_SENSOR_PERMISSION = 1
    }
}
