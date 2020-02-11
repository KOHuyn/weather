package com.kohuyn.weatherapp.ui.splashscreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.core.BaseActivity
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.ui.home.HomeActivity

class SplashScreen :BaseActivity() {
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun updateUI(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        if(!checkPermission()){
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }
    }
    private fun checkPermission():Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1){
            if(permissions.isNotEmpty()){
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}