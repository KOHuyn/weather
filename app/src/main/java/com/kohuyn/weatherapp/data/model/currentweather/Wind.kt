package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class Wind(
    @Expose val deg: Int,
    @Expose val speed: Double
)