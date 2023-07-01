package com.example.aparking

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.launch

class MapViewModel(private val repository: ParkingSpotsRepository) : ViewModel() {
    private var showRouteLiveData = MutableLiveData<Boolean>()
    private var showRouteToLiveData = MutableLiveData<Point>()
    private var showRouteIconLiveData = MutableLiveData(true)
    private var showCurrentLocationLiveData = MutableLiveData<Boolean>()
    private var selectedSpotLiveData = MutableLiveData<String>()
    private var loadLiveData = MutableLiveData<Pair<Int, Int>>()
    private var spotLiveData = MutableLiveData<ParkingSpot>()

    fun showRoute(display: Boolean) {
        showRouteLiveData.postValue(display)
        showRouteIconLiveData.postValue(!display)
    }

    fun showRoute(destination: Point) {
        showRouteToLiveData.postValue(destination)
        showRouteIconLiveData.postValue(false)
    }

    fun showCurrentLocation() {
        showCurrentLocationLiveData.postValue(true)
        showRouteIconLiveData.postValue(false)
    }

    fun selectSpot(name: String) {
        selectedSpotLiveData.postValue(name)
    }

    fun setLoad(occupied: Int, free: Int) {
        loadLiveData.postValue(occupied to free)
    }

    fun setSpot(spot: ParkingSpot) {
        spotLiveData.postValue(spot)
    }

    fun getShowRouteLiveData() = showRouteLiveData

    fun getShowRouteToLiveData() = showRouteToLiveData

    fun getShowRouteIconLiveData() = showRouteIconLiveData

    fun getShowCurrentLocationLiveData() = showCurrentLocationLiveData

    fun getSelectedSpotLiveData() = selectedSpotLiveData

    fun getLoadLiveData() = loadLiveData

    fun getSpotLiveData() = spotLiveData

    // gRPC
    val parkingSpotsLiveData = MutableLiveData<List<ParkingSpot>>()

    fun fetchParkingSpots() {
        viewModelScope.launch {
            val response = repository.getParkingSpots()
            if (response.isSuccessful) {
                parkingSpotsLiveData.postValue(response.body())
            } else {
                // обработать ошибку
            }
        }
    }
}