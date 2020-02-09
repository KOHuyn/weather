package com.kohuyn.weatherapp.data.model.city

import com.google.gson.annotations.Expose

data class City (
    @Expose
    val id:Int,
    @Expose
    val name:String,
    @Expose
    val country:String
)