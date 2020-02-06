package com.kohuyn.weatherapp.data.model.hourweather

import com.google.gson.annotations.Expose

data class Main(
    @Expose val grnd_level: Double,
    @Expose val humidity: Int,
    @Expose val pressure: Double,
    @Expose val sea_level: Double,
    @Expose val temp: Double,
    @Expose val temp_kf: Double,
    @Expose val temp_max: Double,
    @Expose val temp_min: Double
)