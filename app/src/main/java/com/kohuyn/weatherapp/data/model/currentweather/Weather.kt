package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class Weather(
    @Expose val description: String,
    @Expose val icon: String,
    @Expose val id: Int,
    @Expose val main: String
)