package com.example.aparking

class ParkingSpotsRepository {
    private val parkingSpots: MutableList<ParkingSpot> = mutableListOf()

    init {
        generateSpots()
    }

    private fun generateSpots(): Unit{

    }

    fun getParkingSpots(): MutableList<ParkingSpot> = parkingSpots
}