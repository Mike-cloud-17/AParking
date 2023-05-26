package com.example.aparking

import junit.framework.Assert.assertEquals
import org.junit.Test

class MyTestClass {
    @Test
    fun getParkingSpotsTest() {
        val spots = ParkingSpotsRepository().getParkingSpots()
        assertEquals(1000, spots.size)
        spots.forEachIndexed { index, spot ->
            assertEquals(index.toLong(), spot.id)
        }
    }

    @Test
    fun getParkingSpotsTest2() {
        val spots = ParkingSpotsRepository().getParkingSpots()
        spots.take(5).forEach { spot ->
            println("ID: ${spot.id}")
            println("Latitude: ${spot.latitude}")
            println("Longitude: ${spot.longitude}")
            println("Is occupied: ${spot.isOccupied}")
            println("Distance to spot: ${spot.distanceToSpot}")
            println("--------------------")
        }
    }
}