package com.kohuyn.weatherapp.ui.adsfullscreen

import android.os.Bundle
import com.core.BaseActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.kohuyn.weatherapp.R


class AdsFullScreenActivity:BaseActivity() {

     lateinit var mInterstitialAd:InterstitialAd
    override fun getLayoutId(): Int = R.layout.activity_adsfullscreen

    override fun updateUI(savedInstanceState: Bundle?) {
        mInterstitialAd = InterstitialAd(this@AdsFullScreenActivity)
        mInterstitialAd.adUnitId = getString(R.string.banner_ad_fullscreen_id)
        val adRequest:AdRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("BE67C5471F1F3825AC86C71F21AE1C85")
            .build()
        mInterstitialAd.loadAd(adRequest)
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                finish()
            }
            override fun onAdLoaded() {
                showInterstitial()
            }

            override fun onAdFailedToLoad(p0: Int) {
                finish()
            }
        }
    }
    fun showInterstitial(){
        if(mInterstitialAd.isLoaded){
            mInterstitialAd.show()
        }
    }
}