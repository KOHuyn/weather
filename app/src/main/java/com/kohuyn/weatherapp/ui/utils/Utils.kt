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
    fun subHour(s:String):String{
        val hour = s.substring(11,13).toInt()
        if(hour==18){
            return "1:00"
        }
        if(hour ==21){
            return "4:00"
        }
        return "${hour+7}:00"
    }
    fun convertMstoKmh(ms:Double):Float{
        val kmh = ms/0.27777778
        return kmh.toFloat()
    }
    fun convertLongToTime(time: Long): String {
        val date = Date(time*1000)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
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
        return when(subDay){
                "Mon" -> "Thứ 2"
                "Tue" -> "Thứ 3"
                "Wed" -> "Thứ 4"
                "Thu" -> "Thứ 5"
                "Fri" -> "Thứ 6"
                "Sat" -> "Thứ 7"
                "Sun" -> "Chủ nhật"
            else -> "nothing"
        }
    }
}