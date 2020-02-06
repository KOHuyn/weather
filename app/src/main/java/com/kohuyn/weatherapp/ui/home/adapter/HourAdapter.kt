package com.kohuyn.weatherapp.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.HourWeather
import com.utils.ext.inflateExt
import kotlinx.android.synthetic.main.item_hour_weather.view.*

class HourAdapter : RecyclerView.Adapter<HourAdapter.HourViewHolder>() {
    private var listHourWeather: MutableList<HourWeather>

    init {
        this.listHourWeather = arrayListOf()
    }

    private fun setIconWeatherHour(icon:String):Int{
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
    fun setListData(list: List<HourWeather>) {
        this.listHourWeather = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class HourViewHolder(var itemViewBinding: View) :
        RecyclerView.ViewHolder(itemViewBinding) {
        fun onBind(item: HourWeather) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder =
        HourViewHolder(
            parent.inflateExt(
                R.layout.item_hour_weather
            )
        )

    override fun getItemCount(): Int = listHourWeather.size

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val item = listHourWeather[holder.adapterPosition]
        holder.onBind(item)
        with(holder.itemView){
            txtTimerHour.text = item.date_time
            txtTemperature.text = "${item.temp}°"
            imgIconDescription.setImageDrawable(resources.getDrawable(setIconWeatherHour(item.icon)))
        }
    }


}