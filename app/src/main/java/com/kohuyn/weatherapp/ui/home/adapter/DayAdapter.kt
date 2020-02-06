package com.kohuyn.weatherapp.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.DayWeather
import com.utils.ext.inflateExt
import kotlinx.android.synthetic.main.item_day_weather.view.*

class DayAdapter : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {
    private var listDayWeather:MutableList<DayWeather>
    init {
        this.listDayWeather = arrayListOf()
    }
    private fun setIconWeatherDay(icon:String):Int{
        return when(icon){
            "01d"-> R.drawable.ic_02
            "01n"-> R.drawable.ic_02
            "02d"-> R.drawable.ic_02
            "02n"-> R.drawable.ic_02
            "03d"->R.drawable.ic_02
            "03n"-> R.drawable.ic_02
            "04d"-> R.drawable.ic_04
            "04n"-> R.drawable.ic_04
            "09d"-> R.drawable.ic_09
            "09n"-> R.drawable.ic_09
            "10d"-> R.drawable.ic_09
            "10n"-> R.drawable.ic_09
            "11d"-> R.drawable.ic_11
            "11n"-> R.drawable.ic_11
            "13d"-> R.drawable.ic_13
            "13n"-> R.drawable.ic_13
            "50d"-> R.drawable.ic_13
            "50n"-> R.drawable.ic_13
            else -> R.drawable.ic_02
        }
    }
    fun setListData(list:List<DayWeather>){
        this.listDayWeather = list.toMutableList()
    }
    inner class DayViewHolder(var itemViewBinding:View):RecyclerView.ViewHolder(itemViewBinding){
        fun onBind(item:DayWeather){}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder =
        DayViewHolder(parent.inflateExt(R.layout.item_day_weather))

    override fun getItemCount(): Int = listDayWeather.size

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = listDayWeather[holder.adapterPosition]
        holder.onBind(item)
        with(holder.itemView){
            txtTemperature.text = "${item.temp}°"
            txtDateTime.text = item.date_time
            imgIconDescription.setImageDrawable(resources.getDrawable(setIconWeatherDay(item.icon)))
        }
    }
}