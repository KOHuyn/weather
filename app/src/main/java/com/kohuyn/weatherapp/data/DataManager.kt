package com.kohuyn.weatherapp.data

import com.kohuyn.weatherapp.data.local.prefs.PrefsHelper
import com.kohuyn.weatherapp.data.remote.ApiHelper

interface DataManager:ApiHelper,PrefsHelper {
}