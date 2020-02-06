package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class Main(
    @Expose val feels_like: Double,
    @Expose val humidity: Int,
    @Expose val pressure: Int,
    @Expose val temp: Double,
    @Expose val temp_max: Double,
    @Expose val temp_min: Double
)