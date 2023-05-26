package com.example.aparking

import kotlin.random.Random

class ParkingSpotsRepository {
    private var parkingSpots: MutableList<ParkingSpot> = mutableListOf()

    init {
        generateSpots()
    }

    private fun generateSpots(): Unit {
        val latRange = 43.21..43.30
        val lonRange = 76.85..77.03
        for (i in 0 until 1000) {
            val latitude = Random.nextDouble(latRange.start, latRange.endInclusive)
            val longitude = Random.nextDouble(lonRange.start, lonRange.endInclusive)
            val isOccupied = Random.nextBoolean()
            val distanceToSpot = Random.nextLong(20, 5001)
            parkingSpots.add(
                ParkingSpot(
                    i.toLong(), latitude = latitude, longitude = longitude,
                    isOccupied = isOccupied, distanceToSpot = distanceToSpot
                )
            )
        }
    }

    fun getParkingSpots(): MutableList<ParkingSpot> = parkingSpots
}