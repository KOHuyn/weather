package com.kohuyn.weatherapp.data.model.hourweather

import com.google.gson.annotations.Expose

data class ParentHourWeather(
    @Expose val cnt: Int,
    @Expose val cod: String,
    @Expose val list: List<X>,
    @Expose val message: Double
)