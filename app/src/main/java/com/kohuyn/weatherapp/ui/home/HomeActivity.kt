package com.kohuyn.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.Resource
import com.core.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.Country
import com.kohuyn.weatherapp.data.model.DayWeather
import com.kohuyn.weatherapp.data.model.HourWeather
import com.kohuyn.weatherapp.data.model.city.City
import com.kohuyn.weatherapp.data.model.currentweather.ParentCurrentWeather
import com.kohuyn.weatherapp.data.model.hourweather.ParentHourWeather
import com.kohuyn.weatherapp.ui.dialog.addcity.AddCityDialog
import com.kohuyn.weatherapp.ui.dialog.delete.DeleteDialog
import com.kohuyn.weatherapp.ui.dialog.gpsdialog.TurnOnGPSDialog
import com.kohuyn.weatherapp.ui.home.adapter.CountryAdapter
import com.kohuyn.weatherapp.ui.home.adapter.DayAdapter
import com.kohuyn.weatherapp.ui.home.adapter.HourAdapter
import com.kohuyn.weatherapp.ui.utils.Utils
import com.utils.KeyboardUtils
import com.utils.ext.setVisibility
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.bottom_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.navigation_view.*
import org.json.JSONArray
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.round

class HomeActivity : BaseActivity(), LocationListener, TurnOnGPSDialog.OnDialogCallback,
    DeleteDialog.OnDialogCallback, AddCityDialog.OnDialogCallback {


    lateinit var sheetBehavior: BottomSheetBehavior<RelativeLayout>
    private val homeViewModel by viewModel<HomeViewModel>()
    private lateinit var adapterHourWeather: HourAdapter
    private lateinit var adapterDayWeather: DayAdapter
    private lateinit var adapterCountry: CountryAdapter
    private lateinit var parentCurrentWeather: ParentCurrentWeather
    private lateinit var parentWeather: ParentHourWeather
    private var listHour: ArrayList<HourWeather> = arrayListOf()
    private var listDay: ArrayList<DayWeather> = arrayListOf()
    private var listCity: ArrayList<Country> = arrayListOf()
    private var listCityPicker: ArrayList<City> = arrayListOf()
    private lateinit var locationManager: LocationManager
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    var isCollapse = false
    var timeZone = 25200
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun updateUI(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
//        checkPermission()
        setLocation()
        setGPS()
        setDataViewModel()
        setBottomSheetCallBack()
        setNav()
        setRcvCountry()
        addJson()
        getListCity()
        Log.e("abc", homeViewModel.getCity())
        KeyboardUtils.hideKeyBoardWhenClickOutSide(window.decorView.rootView, this)
    }

    private fun addJson() {
        var json: String? = null
        try {
            val inputStream: InputStream = assets.open("city.json")
            json = inputStream.bufferedReader().use { it.readText() }

            var jsonArr = JSONArray(json)

            for (i in 0..jsonArr.length() - 1) {
                var jsonObj = jsonArr.getJSONObject(i)
                listCityPicker.add(
                    City(
                        jsonObj.getInt("id"),
                        jsonObj.getString("name"),
                        jsonObj.getString("country")
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onSendData(isCheck: Boolean) {
        if (isCheck) {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        setLocation()
//        setDataViewModel()
//    }

    private fun setDataViewModel() {
        addDispose(homeViewModel.getCurrentWeather(lat, lon),
            homeViewModel.output.resultCurrentWeather.subscribe {
                parentCurrentWeather = it
//                txt_name_city.text = parentCurrentWeather.name
//                txtDestination.text = parentCurrentWeather.weather[0].description
                txt_temp.text = Utils.convertKtoC(parentCurrentWeather.main.temp).toInt().toString()
                txt_wind.text =
                    "${round(Utils.convertMstoKmh(parentCurrentWeather.wind.speed)).toInt()} km/h"
                txt_clouds.text = "${parentCurrentWeather.clouds.all}%"
                txt_humidity.text = "${parentCurrentWeather.main.humidity}%"
                txt_sunTime.text =
                    "${Utils.convertLongToTime(parentCurrentWeather.sys.sunrise)} - ${Utils.convertLongToTime(
                        parentCurrentWeather.sys.sunset
                    )}"
                timeZone = parentCurrentWeather.timezone
                val iconWeather = parentCurrentWeather.weather[0].icon
                setIconWeather(iconWeather)
                animationCollapse()
                img_description.animate().alpha(1f).duration = 300
            }
        )

        addDispose(homeViewModel.getHourWeather(lat, lon),
            homeViewModel.output.resultHourWeather.subscribe {
                parentWeather = it
                for (i in 0..9) {
                    listHour.add(
                        HourWeather(
                            Utils.convertKtoC(parentWeather.list[i].main.temp),
                            parentWeather.list[i].weather[0].icon,
                            Utils.subHour(parentWeather.list[i].dt_txt, timeZone)
                        )
                    )
                }
                setRcvHourWeather()
                for (i in 0..39 step 8) {
                    listDay.add(
                        DayWeather(
                            Utils.convertKtoC(parentWeather.list[i].main.temp),
                            parentWeather.list[i].weather[0].icon, dayOfWeek(
                                Utils.convertLongToCalendar(parentWeather.list[i].dt)
                            )
                        )
                    )
                }
                setRcvDayWeather()
            })
    }

    private fun dayOfWeek(day: String): String {
        return when (day) {
            "Mon" -> resources.getText(R.string.str_monday).toString()
            "Tue" -> resources.getText(R.string.str_tuesday).toString()
            "Wed" -> resources.getText(R.string.str_wednesday).toString()
            "Thu" -> resources.getText(R.string.str_thursday).toString()
            "Fri" -> resources.getText(R.string.str_friday).toString()
            "Sat" -> resources.getText(R.string.str_saturday).toString()
            "Sun" -> resources.getText(R.string.str_sunday).toString()
            else -> resources.getText(R.string.str_monday).toString()
        }
    }

    private fun setIconWeather(icon: String) {
        when (icon) {
            "01d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_1d))
                txtDestination.text = resources.getText(R.string.str_clear_sky)
            }
            "01n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_1d))
                txtDestination.text = resources.getText(R.string.str_clear_sky)
            }
            "02d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_2d))
                txtDestination.text = resources.getText(R.string.str_few_clouds)
            }
            "02n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_2n))
                txtDestination.text = resources.getText(R.string.str_few_clouds)
            }
            "03d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_3d))
                txtDestination.text = resources.getText(R.string.str_scattered_clouds)
            }
            "03n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_3d))
                txtDestination.text = resources.getText(R.string.str_scattered_clouds)
            }
            "04d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_4d))
                txtDestination.text = resources.getText(R.string.str_broken_clouds)
            }
            "04n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_4d))
                txtDestination.text = resources.getText(R.string.str_broken_clouds)
            }
            "09d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_9d))
                txtDestination.text = resources.getText(R.string.str_shower_rain)
            }
            "09n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_9d))
                txtDestination.text = resources.getText(R.string.str_shower_rain)
            }
            "10d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_10d))
                txtDestination.text = resources.getText(R.string.str_rain)
            }
            "10n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_10d))
                txtDestination.text = resources.getText(R.string.str_rain)
            }
            "11d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_11d))
                txtDestination.text = resources.getText(R.string.str_thunderstorm)
            }
            "11n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_11d))
                txtDestination.text = resources.getText(R.string.str_thunderstorm)
            }
            "13d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_13d))
                txtDestination.text = resources.getText(R.string.str_snow)
            }
            "13n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_13d))
                txtDestination.text = resources.getText(R.string.str_snow)
            }
            "50d" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_50d))
                txtDestination.text = resources.getText(R.string.str_mist)
            }
            "50n" -> {
                img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_50d))
                txtDestination.text = resources.getText(R.string.str_mist)
            }
        }
    }

    private fun setNav() {
        img_bgWeather.setOnClickListener { drawerLayout.openDrawer(GravityCompat.END) }

        etd_search.setOnClickListener {
            val cityDialog = AddCityDialog()
            cityDialog.setListCity(listCityPicker)
            cityDialog.setOnDialogCallback(this@HomeActivity)
            cityDialog.show(supportFragmentManager, "addCity")
        }
    }

    private fun setBottomSheetCallBack() {
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {

                if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    if (isCollapse) {
                        animationCollapse()
                        val animation =
                            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.top_down)
                        txtSwipe.text = resources.getString(R.string.str_swipe_up_to_detail)
                        txtSwipe.startAnimation(animation)
                    }
                    isCollapse = false
                }
                if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    if (!isCollapse) {
                        animationExpand()
                        val animation =
                            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.down_top)
                        txtSwipe.text = resources.getString(R.string.str_swipe_down_to_home)
                        txtSwipe.startAnimation(animation)
                    }
                    isCollapse = true
                }

            }
        })
    }

    fun animationExpand() {
        val animation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_out_left)
        scrollViewWeather.startAnimation(animation)
        img_bgWeather.startAnimation(animation)
        val animDestination =
            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_out_right)
        txtDestination.startAnimation(animDestination)
        txt_name_city.startAnimation(animDestination)
        txtDestination.setVisibility(false)
        scrollViewWeather.setVisibility(false)
        img_bgWeather.setVisibility(false)
        txt_name_city.setVisibility(false)
        val rcvHourAnimation =
            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_from_right)
        rcvHourWeather.startAnimation(rcvHourAnimation)
        val rcvDayAnimation =
            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_from_left)
        cvRcvDay.startAnimation(rcvDayAnimation)
        rcvHourWeather.setVisibility(true)
        cvRcvDay.setVisibility(true)
    }

    fun animationCollapse() {
        val animScroll = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_from_right)
        scrollViewWeather.startAnimation(animScroll)
        img_bgWeather.startAnimation(animScroll)
        val animDestination =
            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_from_left)
        txtDestination.startAnimation(animDestination)
        txt_name_city.startAnimation(animDestination)
        txtDestination.setVisibility(true)
        scrollViewWeather.setVisibility(true)
        img_bgWeather.setVisibility(true)
        txt_name_city.setVisibility(true)

        val rcvHourAnimation =
            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_out_left)
        rcvHourWeather.startAnimation(rcvHourAnimation)
        val rcvDayAnimation =
            AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_out_right)
        cvRcvDay.startAnimation(rcvDayAnimation)
        rcvHourWeather.setVisibility(false)
        cvRcvDay.setVisibility(false)
    }

    private fun setRcvHourWeather() {
        rcvHourWeather.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterHourWeather = HourAdapter()
        adapterHourWeather.setListData(listHour)
        rcvHourWeather.adapter = adapterHourWeather
    }

    private fun setRcvDayWeather() {
        rcvDayWeather.layoutManager = LinearLayoutManager(this)
        adapterDayWeather = DayAdapter()
        adapterDayWeather.setListData(listDay)
        rcvDayWeather.adapter = adapterDayWeather
    }

    fun setWeatherCity(position: Int) {
        addDispose(
            homeViewModel.getCurrentWeatherId(listCity[position].idCountry),
            homeViewModel.output.resultCurrentWeather.subscribe {
                parentCurrentWeather = it
                txt_name_city.text = parentCurrentWeather.name
                txtDestination.text = parentCurrentWeather.weather[0].description
                txt_temp.text = Utils.convertKtoC(parentCurrentWeather.main.temp).toInt().toString()
                txt_wind.text =
                    "${round(Utils.convertMstoKmh(parentCurrentWeather.wind.speed)).toInt()} km/h"
                txt_clouds.text = "${parentCurrentWeather.clouds.all}%"
                txt_humidity.text = "${parentCurrentWeather.main.humidity}%"
                txt_sunTime.text =
                    "${Utils.convertLongToTime(parentCurrentWeather.sys.sunrise)} - ${Utils.convertLongToTime(
                        parentCurrentWeather.sys.sunset
                    )}"
                timeZone = parentCurrentWeather.timezone
                val iconWeather = parentCurrentWeather.weather[0].icon
                setIconWeather(iconWeather)
                animationCollapse()
                img_description.animate().alpha(1f).duration = 300
            })
    }

    private fun setRcvCountry() {
        rcvCountry.layoutManager = LinearLayoutManager(this)
        adapterCountry = CountryAdapter()
        adapterCountry.setListData(listCity)
        rcvCountry.adapter = adapterCountry
        adapterCountry.setOnItemClick(object : CountryAdapter.OnItemClick {
            override fun onItemClickListener(view: View, position: Int) {
                setWeatherCity(position)
                Handler().postDelayed({
                    drawerLayout.closeDrawers()
                },200)
            }
        }
        )
    }

    private fun setGPS() {
        val manager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            toast("No GPS")
            val gps = TurnOnGPSDialog()
            gps.setOnDialogCallback(this)
            gps.isCancelable = true
            gps.show(supportFragmentManager, "ok")
        }
    }

    private fun getLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val lastKnownLocationGPS: Location? =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            return lastKnownLocationGPS
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
            return null
        }
    }

    private fun setLocation() {

        onLocationChanged(getLocation())
        if (getLocation() != null) {
            lon = getLocation()!!.longitude
            lat = getLocation()!!.latitude
        } else {
            toast("Location Null")
        }
        titleMap(lat, lon)
    }

    private fun titleMap(lat: Double, lon: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addrs: List<Address> = geocoder.getFromLocation(lat, lon, 1)
            if (addrs.isNotEmpty()) {
                val returnedAddr: Address = addrs[0]
                txt_name_city.text = "${returnedAddr.thoroughfare}-${returnedAddr.subAdminArea}"
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }

    override fun onLocationChanged(p0: Location?) {
        if (p0 != null) {
            lon = p0.longitude
            lat = p0.latitude
        }
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }

    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    override fun onSendDataDelete(isYes: Boolean, position: Int) {
        if (isYes) {
            listCity.removeAt(position)
            adapterCountry.notifyDataSetChanged()
            toast("delete")
        }
    }

    override fun onSendDataAddCity(id: Int, name: String, country: String) {
        if (homeViewModel.getCity() == "") {
            homeViewModel.setCity("123")
        }
        homeViewModel.setCity("${homeViewModel.getCity()} $id")
        Log.e("abcd", homeViewModel.getCity())
        getListCity()
    }

    private fun getListCity() {
        listCity.clear()
        if (homeViewModel.getCity() != "") {
            var id: Int
            val listId = homeViewModel.getCity()?.split(" ") as ArrayList<String>
            var arrTemp:ArrayList<String> = arrayListOf()
            for(i in listId.indices){
                if(!arrTemp.contains(listId[i])){
                    arrTemp.add(listId[i])
                }
            }
            listId.clear()
            listId.addAll(arrTemp)
            for (i in listId.indices) {
                id = listId[i].toInt()
                for (x in listCityPicker.indices) {
                    if (id == listCityPicker[x].id) {
                        listCity.add(
                            Country(
                                listCityPicker[x].id,
                                listCityPicker[x].name,
                                listCityPicker[x].country
                            )
                        )
                    }
                }
            }

            setRcvCountry()
        }
    }
}