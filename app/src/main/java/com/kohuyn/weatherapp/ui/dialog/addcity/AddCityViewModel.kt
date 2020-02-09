package com.kohuyn.weatherapp.ui.dialog.addcity

import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.weatherapp.data.DataManager
import com.kohuyn.weatherapp.data.model.city.City
import com.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.IOException
import java.io.InputStream

interface AddCityInput

interface AddCityOutput{
    val resultCity:Observable<List<City>>
}
class AddCityViewModel(dataManager: DataManager,schedulerProvider: SchedulerProvider,var gson:Gson= Gson()):BaseViewModel<AddCityInput,AddCityOutput,DataManager>(dataManager, schedulerProvider),AddCityInput,AddCityOutput{
    override val input: AddCityInput
        get() = this
    override val output: AddCityOutput
        get() = this
    override val resultCity: PublishSubject<List<City>> = PublishSubject.create()

}