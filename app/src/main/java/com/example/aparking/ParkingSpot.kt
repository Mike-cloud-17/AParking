package com.example.aparking

import java.time.LocalDateTime

data class ParkingSpot(
    val id: Long?,
    val sensorID: Long? = null,
    var isOccupied: Boolean?,
    var SpotNumber: String? = null,
    var startTime: LocalDateTime? = null,
    var endTime: LocalDateTime? = null,
    val latitude: Double?,
    val longitude: Double?,
    var currentUserId: Long? = null,
    var currentCarNumber: String? = null,
    val address: String? = "г. Алматы", // for demo
    var distanceToSpot: Long? // for demo
) {
}