package com.example.aparking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {
    private var showRouteLiveData = MutableLiveData<Boolean>()

    fun showRoute() {
        showRouteLiveData.postValue(true)
    }


    fun getShowRouteLiveData() = showRouteLiveData
}