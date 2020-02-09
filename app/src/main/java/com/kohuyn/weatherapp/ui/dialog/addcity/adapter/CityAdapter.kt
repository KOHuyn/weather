package com.kohuyn.weatherapp.ui.dialog.addcity.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.Country
import com.kohuyn.weatherapp.data.model.city.City
import com.utils.ext.inflateExt
import kotlinx.android.synthetic.main.item_city.view.*

class CityAdapter : RecyclerView.Adapter<CityAdapter.CityViewHolder>(), Filterable {
    private var listCountry: MutableList<City> = arrayListOf()
    var suggestion: List<City>
    private lateinit var onItemClick: OnItemClickCity

    init {
        this.suggestion = arrayListOf()
    }

    fun setOnItemClick(listener: OnItemClickCity) {
        this.onItemClick = listener
    }

    fun setListData(list: List<City>) {
        this.listCountry = list.toMutableList()
        this.suggestion = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class CityViewHolder(var itemViewBinding: View) :
        RecyclerView.ViewHolder(itemViewBinding) {
        fun onBind(item: City) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder =
        CityViewHolder(parent.inflateExt(R.layout.item_city))

    override fun getItemCount(): Int = suggestion.size


    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val item = listCountry[holder.adapterPosition]
        holder.onBind(item)
        with(holder.itemView){
            txtNameCity.text = item.name
        }

        holder.itemViewBinding.setOnClickListener {
            onItemClick.onItemClickListener(item.id,item.name,item.country,position)
        }
    }


    interface OnItemClickCity{
        fun onItemClickListener(id:Int, name:String,country:String, position: Int)
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    suggestion = listCountry
                } else {
                    val filterList = ArrayList<City>()
                    for (city in listCountry) {
                        if (city.name.toLowerCase().contains(charString.toLowerCase())) {
                            filterList.add(city)
                        }
                    }
                    suggestion = filterList
                }
                val filterResult = FilterResults()
                filterResult.values = suggestion
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                suggestion = results?.values as List<City>
                notifyDataSetChanged()
            }

        }
    }
}