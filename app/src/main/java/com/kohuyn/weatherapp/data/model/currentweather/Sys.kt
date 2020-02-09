package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class Sys(
    @Expose val country: String,
    @Expose val id: Int,
    @Expose val sunrise: Long,
    @Expose val sunset: Long,
    @Expose val type: Int
)