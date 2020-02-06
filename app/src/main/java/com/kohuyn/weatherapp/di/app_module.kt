package com.kohuyn.weatherapp.di

import com.google.gson.GsonBuilder
import com.kohuyn.weatherapp.R
import com.kohuyn.weatherapp.data.AppDataManager
import com.kohuyn.weatherapp.data.DataManager
import com.kohuyn.weatherapp.data.local.prefs.AppPrefsHelper
import com.kohuyn.weatherapp.data.local.prefs.PrefsHelper
import com.kohuyn.weatherapp.data.remote.ApiHelper
import com.kohuyn.weatherapp.data.remote.AppApiHelper
import com.kohuyn.weatherapp.ui.home.HomeViewModel
import com.utils.SchedulerProvider
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

val appModule: Module = module {
    single {
        GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'X'").create()!!
    }
    single {
        CalligraphyConfig.Builder().setDefaultFontPath("font/segui/seguisb_regular.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()
    }
    single { SchedulerProvider() }
    single { AppPrefsHelper(get(), "kohuyn", get()) as PrefsHelper }
    single { AppApiHelper() as ApiHelper }
    single { AppDataManager(get(), get()) as DataManager }
    single {
        //room
    }
}
val viewModule: Module = module {
            viewModel { HomeViewModel( get(),get(),get()) }
}
val weatherModule = listOf(appModule, viewModule)