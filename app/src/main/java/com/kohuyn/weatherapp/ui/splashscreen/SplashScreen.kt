package com.kohuyn.weatherapp.ui.splashscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
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
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}