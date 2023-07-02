package com.example.aparking

import com.example.aparking.authorization.APIService
import retrofit2.Response
import kotlin.random.Random

//class ParkingSpotsRepository(private val apiService: APIService) {
class ParkingSpotsRepository() {
    private var parkingSpots: MutableList<ParkingSpot> = mutableListOf()

    init {
        generateSpots()
    }

    private fun generateSpots(): Unit {
        // generate Almaty points
        var latRange = 43.21..43.30
        var lonRange = 76.85..77.03
        val alphabet = ('A'..'Z').toList()
        for (i in 0 until 200) {
            val latitude = Random.nextDouble(latRange.start, latRange.endInclusive)
            val longitude = Random.nextDouble(lonRange.start, lonRange.endInclusive)
            val isOccupied = Random.nextBoolean()
            val distanceToSpot = Random.nextLong(20, 5001)
            val spotNumber = "${alphabet.random()} ${(1..100).random()}"
            parkingSpots.add(
                ParkingSpot(
                    i.toLong(), spotNumber = spotNumber, latitude = latitude, longitude = longitude,
                    isOccupied = isOccupied, distanceToSpot = distanceToSpot, address = "г. Алматы",
                )
            )
        }

        // Generate Moscow points
        latRange = 55.70..55.77
        lonRange = 37.58..37.76
        for (i in 200 until 1199) {
            val latitude = Random.nextDouble(latRange.start, latRange.endInclusive)
            val longitude = Random.nextDouble(lonRange.start, lonRange.endInclusive)
            val isOccupied = Random.nextBoolean()
            val distanceToSpot = Random.nextLong(20, 5001)
            val spotNumber = "${alphabet.random()} ${(1..100).random()}"
            parkingSpots.add(
                ParkingSpot(
                    i.toLong(), spotNumber = spotNumber, latitude = latitude, longitude = longitude,
                    isOccupied = isOccupied, distanceToSpot = distanceToSpot, address = "г. Москва",
                )
            )
        }
    }

    fun getParkingSpots(): MutableList<ParkingSpot> = parkingSpots
}


// gRPC
//    suspend fun addNewParkingSpot(parkingSpot: ParkingSpot): Response<Void> {
//        return apiService.addNewParkingSpot(parkingSpot)
//    }
//
////    suspend fun getParkingSpots(): Response<List<ParkingSpot>> {
////        return apiService.getParkingSpots()
////    }
//
//    suspend fun getParkingSpotById(parkingId: Long): Response<ParkingSpot> {
//        return apiService.getParkingSpotById(parkingId)
//    }
//
//    suspend fun getAvailableParkingSpots(latitude: Double, longitude: Double, radius: Double): Response<List<ParkingSpot>> {
//        return apiService.getAvailableParkingSpots(latitude, longitude, radius)
//    }
//
//    suspend fun getPossibleParkingSpots(): Response<List<ParkingSpot>> {
//        return apiService.getPossibleParkingSpots()
//    }
//
//    suspend fun checkGrpcConnection(id: Long): Response<Void> {
//        return apiService.checkGrpcConnection(id)
//    }
//}