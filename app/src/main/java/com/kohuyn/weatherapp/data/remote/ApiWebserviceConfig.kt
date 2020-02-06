package com.kohuyn.weatherapp.data.remote

object ApiWebserviceConfig {
    /*=============================URL===================================*/
    const val APIKEY = "40ea9dcac874cec874ecd2d8708eafeb"
    const val BASE_URL = "https://api.openweathermap.org"
    const val URL_LAT_LONG = "$BASE_URL/data/2.5/weather"
    const val URL_HOUR="$BASE_URL/data/2.5/forecast"
    /*==============================REQUEST==================================*/
    const val SUSCESS = 200
    const val ERROR = 401
}