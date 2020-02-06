package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class Sys(
    @Expose val country: String,
    @Expose val id: Int,
    @Expose val sunrise: Int,
    @Expose val sunset: Int,
    @Expose val type: Int
)