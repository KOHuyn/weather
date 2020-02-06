package com.kohuyn.weatherapp.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.BaseActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.Country
import com.kohuyn.weatherapp.data.model.DayWeather
import com.kohuyn.weatherapp.data.model.HourWeather
import com.kohuyn.weatherapp.data.model.currentweather.ParentCurrentWeather
import com.kohuyn.weatherapp.data.model.hourweather.ParentHourWeather
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
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.util.*
import kotlin.math.round

class HomeActivity : BaseActivity(),LocationListener {
    lateinit var sheetBehavior: BottomSheetBehavior<RelativeLayout>
    private val homeViewModel by viewModel<HomeViewModel>()
    private lateinit var adapterHourWeather: HourAdapter
    private lateinit var adapterDayWeather: DayAdapter
    private lateinit var adapterCountry: CountryAdapter
    private lateinit var  parentCurrentWeather: ParentCurrentWeather
    private lateinit var  parentWeather: ParentHourWeather
    private var listHour: ArrayList<HourWeather> = arrayListOf()
    private var listDay: ArrayList<DayWeather> = arrayListOf()
    private var listCity: ArrayList<Country> = arrayListOf()
    private lateinit var locationManager:LocationManager
    private var lat:Double = 0.0
    private var lon:Double = 0.0
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun updateUI(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        checkPermission()
        setLocation()
        setDataViewModel()
        setBottomSheetCallBack()
        init()
        setNav()
        setRcvCountry()
        setTheme()
        KeyboardUtils.hideKeyBoardWhenClickOutSide(window.decorView.rootView, this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        setLocation()
        setDataViewModel()
    }
    private fun setDataViewModel(){
        addDispose(homeViewModel.getCurrentWeather(lat,lon),
            homeViewModel.output.resultCurrentWeather.subscribe{
                parentCurrentWeather = it
//                txt_name_city.text = parentCurrentWeather.name
                txtDestination.text = parentCurrentWeather.weather[0].description
                txt_temp.text = Utils.convertKtoC(parentCurrentWeather.main.temp).toInt().toString()
                txt_wind.text =  "${round(Utils.convertMstoKmh(parentCurrentWeather.wind.speed)).toInt()} km/h"
                txt_clouds.text ="${parentCurrentWeather.clouds.all}%"
                txt_humidity.text = "${parentCurrentWeather.main.humidity}%"
                val iconWeather = parentCurrentWeather.weather[0].icon
                setIconWeather(iconWeather)
            }
        )
        addDispose(homeViewModel.getHourWeather(lat,lon ),
            homeViewModel.output.resultHourWeather.subscribe {
                parentWeather = it
                for (i in 0..9){
                    listHour.add(HourWeather(Utils.convertKtoC(parentWeather.list[i].main.temp),parentWeather.list[i].weather[0].icon,Utils.subHour(parentWeather.list[i].dt_txt)))
                }
                setRcvHourWeather()
                for (i in 0..39 step 8){
                    listDay.add(DayWeather(Utils.convertKtoC(parentWeather.list[i].main.temp),parentWeather.list[i].weather[0].icon,Utils.convertLongToCalendar(parentWeather.list[i].dt)))
                }
                setRcvDayWeather()
            })
    }
    private fun setIconWeather(icon:String){
        when(icon){
            "01d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_1d))}
            "01n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_1d))}
            "02d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_2d))}
            "02n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_2n))}
            "03d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_3d))}
            "03n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_3d))}
            "04d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_4d))}
            "04n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_4d))}
            "09d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_9d))}
            "09n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_9d))}
            "10d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_10d))}
            "10n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_10d))}
            "11d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_11d))}
            "11n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_11d))}
            "13d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_13d))}
            "13n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_13d))}
            "50d"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_50d))}
            "50n"-> {img_description.setImageDrawable(resources.getDrawable(R.drawable.bg_50d))}
        }
    }

    private fun setNav() {
        img_bgWeather.setOnClickListener { drawerLayout.openDrawer(GravityCompat.END) }
    }

    private fun setBottomSheetCallBack() {
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(p0: View, p1: Int) {
                if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    txtDestination.setVisibility(true)
                    scrollViewWeather.setVisibility(true)
                    img_bgWeather.setVisibility(true)
                    txt_name_city.setVisibility(true)
                    txtSwipe.text = "swipe up to detail"
                }
                if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    txtDestination.setVisibility(false)
                    scrollViewWeather.setVisibility(false)
                    img_bgWeather.setVisibility(false)
                    txt_name_city.setVisibility(false)
                    txtSwipe.text = " "
                }
            }
        })
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

    private fun setRcvCountry() {
        rcvCountry.layoutManager = LinearLayoutManager(this)
        adapterCountry = CountryAdapter()
        adapterCountry.setListData(listCity)
        rcvCountry.adapter = adapterCountry
        adapterCountry.setOnItemClick(object : CountryAdapter.OnItemClick {
            override fun onItemClickListener(view: View, position: Int) {
                toast(listCity[position].nameCountry)
            }
        })
    }

    private fun setTheme() {
        navigationView.setBackgroundColor(resources.getColor(R.color.clr_weather_background_pink))
//        bg_bottomSheet.setBackgroundColor((resources.getColor(R.color.clr_weather_background_pink)))
    }

    private fun init() {
        listCity.add(Country("Ha Noi", "123"))
        listCity.add(Country("Vinh Phuc", "123"))
        listCity.add(Country("Ho Chi Minh", "123"))
        listCity.add(Country("Ha Tinh", "123"))
        listCity.add(Country("Nghe An", "123"))
        listCity.add(Country("Hue", "123"))
    }
    private fun checkPermission(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED&&
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
    }
    @SuppressLint("MissingPermission")
    private fun setLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        onLocationChanged(location)
        if (location != null) {
            lon = location.longitude
            lat = location.latitude
            titleMap(lat,lon)
            Log.e("latlon", "$lat - $lon " )
        }else{
            toast("GPS not working")
        }
    }

    private fun titleMap(lat:Double,lon:Double){
        var street:String ?=null
        val geocoder= Geocoder(this, Locale.getDefault())
        try {
            val addrs:List<Address> = geocoder.getFromLocation(lat,lon,1)
            if(addrs.isNotEmpty()){
                val returnedAddr :Address = addrs[0]
                txt_name_city.text = "${returnedAddr.thoroughfare}-${returnedAddr.subAdminArea}"
//                val strReturnedAddress = StringBuilder()
//                for (i in 0.. returnedAddr.maxAddressLineIndex){
//                    strReturnedAddress.append(returnedAddr.getAddressLine(i)).append("")
//                }
//                street = strReturnedAddress.toString()
            }
//            txt_name_city.text = street
        }catch (ex:IOException){
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
}