package com.kohuyn.weatherapp.ui.home.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.Country
import com.kohuyn.weatherapp.ui.home.adapter.CountryAdapter.CountryViewHolder
import com.utils.ext.inflateExt
import kotlinx.android.synthetic.main.item_country.view.*

class CountryAdapter : RecyclerView.Adapter<CountryViewHolder>() {
    private var listCountry: MutableList<Country>
    private lateinit var onItemClick: OnItemClick

    init {
        this.listCountry = arrayListOf()
    }

    fun setOnItemClick(listener: OnItemClick) {
        this.onItemClick = listener
    }

    fun setListData(list: List<Country>) {
        this.listCountry = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class CountryViewHolder(var itemViewBinding: View) :
        RecyclerView.ViewHolder(itemViewBinding) {
        fun onBind(item: Country) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(parent.inflateExt(R.layout.item_country))

    override fun getItemCount(): Int = listCountry.size


    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val item = listCountry[holder.adapterPosition]
        holder.onBind(item)
        with(holder.itemView) {
            txtCity.text = item.nameCountry
            cv_delete.setOnClickListener {
                onItemClick.onDeleteItem(it,position)
            }
        }
        holder.itemViewBinding.setOnClickListener {
            onItemClick.onItemClickListener(it, position)
        }
    }

    interface OnItemClick {
        fun onItemClickListener(view: View, position: Int)
        fun onDeleteItem(view:View,position: Int)
    }
}