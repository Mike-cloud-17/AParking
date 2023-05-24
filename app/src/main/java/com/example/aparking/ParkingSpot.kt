package com.example.aparking

import java.time.LocalDateTime

data class ParkingSpot(
    val id: Long,
    val sensorID: Long,
    var isOccupied: Boolean,
    var SpotNumber: Boolean,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    var latitude: Double,
    val longitude: Double,
    var currentUserId: Long,
    var currentCarNumber: String
) {
}