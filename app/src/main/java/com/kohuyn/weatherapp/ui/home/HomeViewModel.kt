package com.kohuyn.weatherapp.ui.home

import android.util.Log
import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.weatherapp.data.DataManager
import com.kohuyn.weatherapp.data.model.currentweather.ParentCurrentWeather
import com.kohuyn.weatherapp.data.model.hourweather.ParentHourWeather
import com.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface HomeInput

interface HomeOutput {
    val resultCurrentWeather: Observable<ParentCurrentWeather>
    val resultHourWeather: Observable<ParentHourWeather>
}

class HomeViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    var gson: Gson = Gson()
) : BaseViewModel<HomeInput, HomeOutput, DataManager>(dataManager, schedulerProvider), HomeInput,
    HomeOutput {

    override val resultCurrentWeather: PublishSubject<ParentCurrentWeather> =
        PublishSubject.create()
    override val resultHourWeather: PublishSubject<ParentHourWeather> = PublishSubject.create()

    fun getCurrentWeather(lat: Double, long: Double) =
        dataManager.getCurrenWeather(lat, long)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({
                val strJson = it.toString()
                val chresponse = gson.fromJson(strJson, ParentCurrentWeather::class.java)
                Log.e("json", strJson)
                resultCurrentWeather.onNext(chresponse)
            }, {
                it.printStackTrace()
            })

    fun getHourWeather(lat: Double, long: Double) =
        dataManager.getHourWeather(lat, long)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({
                val strJson = it.toString()
                val chresponse = gson.fromJson(strJson, ParentHourWeather::class.java)
                Log.e("json", strJson)
                resultHourWeather.onNext(chresponse)
            }, {
                it.printStackTrace()
            })

    override val input: HomeInput
        get() = this

    override val output: HomeOutput
        get() = this
}