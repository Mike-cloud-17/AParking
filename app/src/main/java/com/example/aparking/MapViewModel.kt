package com.example.aparking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private var showRouteLiveData = MutableLiveData<Boolean>()
    private var showCurrentLocationLiveData = MutableLiveData<Boolean>()
    private var selectedSpotLiveData = MutableLiveData<String>()

    fun showRoute(display: Boolean) {
        showRouteLiveData.postValue(display)
    }

    fun showCurrentLocation() {
        showCurrentLocationLiveData.postValue(true)
    }

    fun selectSpot(name: String) {
        selectedSpotLiveData.postValue(name)
    }

    fun getShowRouteLiveData() = showRouteLiveData

    fun getShowCurrentLocationLiveData() = showCurrentLocationLiveData

    fun getSelectedSpotLiveData() = selectedSpotLiveData
}