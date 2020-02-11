package com.kohuyn.weatherapp.ui.dialog.addcity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.BaseDialog
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.model.city.City
import com.kohuyn.weatherapp.ui.dialog.addcity.adapter.CityAdapter
import kotlinx.android.synthetic.main.dialog_city_picker.*
import kotlinx.android.synthetic.main.navigation_view.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.Normalizer
import java.util.regex.Pattern
import kotlin.math.log

class AddCityDialog:BaseDialog() {
    override fun getLayoutId(): Int = R.layout.dialog_city_picker

    private lateinit var onDialogCallback: OnDialogCallback

    private lateinit var listCity:List<City>

    private lateinit var cityAdapter: CityAdapter

    override fun updateUI(savedInstanceState: Bundle?) {
        serRcv()
        imgCancel.setOnClickListener { dismiss() }
        rlParent.setOnClickListener { dismiss() }
        edtSearch.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                cityAdapter.filter.filter(unAccent(p0.toString()))
            }
        })
    }
    fun setOnDialogCallback(onDialogCallback: OnDialogCallback){
        this.onDialogCallback = onDialogCallback

    }
    fun unAccent(s: String?): String? {
        val temp: String = Normalizer.normalize(s, Normalizer.Form.NFD)
        val pattern: Pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(temp).replaceAll("")
    }
    fun setListCity(listCity:List<City>){
        this.listCity = listCity
    }
    interface OnDialogCallback{
        fun onSendDataAddCity(id:Int, name:String,country:String)
    }
    private fun serRcv(){

        cityAdapter = CityAdapter()
        cityAdapter.setListData(listCity)
        rcvCityDialog.layoutManager = LinearLayoutManager(context)
        rcvCityDialog.adapter = cityAdapter
        cityAdapter.setOnItemClick(object :CityAdapter.OnItemClickCity{
            override fun onItemClickListener(view: View, name: String, position: Int) {
                for(i in listCity.indices){
                    if(name == listCity[i].name){
                        onDialogCallback.onSendDataAddCity(listCity[i].id,listCity[i].name,listCity[i].country)
                        dismiss()

                    }
                }

            }
        })
    }
}