package com.kohuyn.weatherapp.ui.utils

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

object Utils {
    fun convertKtoC(k:Double):Float{
        val c = k - 273.15
        val round = round(c*10)/10
        return round.toFloat()
    }
    fun subHour(s:String,timeZone:Int):String{
        val GMT = timeZone/3600
        val hour = s.substring(11,13).toInt()
        if(hour+GMT>=24){
            return "${hour+GMT-24}:00"
        }
        return "${hour+(timeZone/3600)}:00"
    }
    fun convertMstoKmh(ms:Double):Float{
        val kmh = ms/0.27777778
        return kmh.toFloat()
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time*1000)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    /**
     * chuyển sang múi giờ cụ thể
     * vd : 7:15 12/2/2020
     */
    fun convertLongToTimeZone(time: Long):String{
        val cal :Calendar = Calendar.getInstance()
        val tz :TimeZone = cal.timeZone
        val sdf  = SimpleDateFormat("HH:mm dd/MM/yyyy")
        sdf.timeZone = tz
        val date = Date(time*1000)
        return sdf.format(date)
    }

    /**
     * @return chuyển sang thứ trong ngày
     * @sample thứ 7
     */
    fun convertLongToCalendar(time: Long):String{
        val cal :Calendar = Calendar.getInstance()
        val tz :TimeZone = cal.timeZone
        val sdf  = SimpleDateFormat("HH:mm dd/MM/yyyy")
        sdf.timeZone = tz
        val date = Date(time*1000)
        cal.time = date
        val day_Week =cal.time.toString()
        val subDay = day_Week.substring(0,3)
        return subDay
    }
}