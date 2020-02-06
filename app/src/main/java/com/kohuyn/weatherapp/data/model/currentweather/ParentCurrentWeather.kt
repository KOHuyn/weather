package com.kohuyn.weatherapp.data.model.currentweather
import com.google.gson.annotations.Expose

data class ParentCurrentWeather(
    @Expose
    val base: String,
    @Expose
    val clouds: Clouds,
    @Expose val cod: Int,
    @Expose val coord: Coord,
    @Expose val dt: Int,
    @Expose val id: Int,
    @Expose val main: Main,
    @Expose val name: String,
    @Expose val sys: Sys,
    @Expose val timezone: Int,
    @Expose val visibility: Int,
    @Expose val weather: List<Weather>,
    @Expose val wind: Wind
)