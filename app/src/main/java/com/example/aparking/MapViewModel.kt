package com.example.aparking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private var showRouteLiveData = MutableLiveData<Boolean>()
    private var showCurrentLocationLiveData = MutableLiveData<Boolean>()

    fun showRoute(display: Boolean) {
        showRouteLiveData.postValue(display)
    }

    fun showCurrentLocation() {
        showCurrentLocationLiveData.postValue(true)
    }

    fun getShowRouteLiveData() = showRouteLiveData

    fun getShowCurrentLocationLiveData() = showCurrentLocationLiveData
}