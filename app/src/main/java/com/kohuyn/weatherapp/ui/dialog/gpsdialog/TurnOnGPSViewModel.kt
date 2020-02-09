package com.kohuyn.weatherapp.ui.dialog.gpsdialog

import com.core.BaseViewModel
import com.kohuyn.weatherapp.data.DataManager
import com.utils.SchedulerProvider

interface TurnOnGPSInput

interface TurnOnGPSOutput

class TurnOnGPSViewModel(dataManager: DataManager,schedulerProvider: SchedulerProvider):BaseViewModel<TurnOnGPSInput,TurnOnGPSOutput,DataManager>(dataManager,schedulerProvider),TurnOnGPSInput,TurnOnGPSOutput{
    override val input: TurnOnGPSInput
        get() = this
    override val output: TurnOnGPSOutput
        get() = this
}