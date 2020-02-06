package com.kohuyn.weatherapp.data.model.hourweather

import com.google.gson.annotations.Expose

data class X(
    @Expose val dt_txt: String,
    @Expose val main: Main,
    @Expose val weather: List<Weather>,
    @Expose val dt:Long
)