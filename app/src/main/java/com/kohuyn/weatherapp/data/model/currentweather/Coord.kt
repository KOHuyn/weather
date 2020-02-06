package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class Coord(
    @Expose val lat: Double,
    @Expose val lon: Double
)