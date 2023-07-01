package com.example.aparking.authorization

import com.example.aparking.ParkingSpot
import com.example.aparking.RegisterCar
import com.example.aparking.User
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @POST("/auth/request")
    fun sendAuthRequest(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("/auth/register")
    fun registerUser(@Body userRegistration: UserRegistration): Call<RegisterResponse>

    @POST("/auth/authenticate")
    fun authenticateUser(@Body authenticateRequest: AuthenticateRequest): Call<AuthenticateResponse>

    @GET("/users/byPhone")
    fun getUserByPhone(@Query("phoneNumber") phoneNumber: String): Call<User>

    // Добавляем новые методы для работы с парковочными местами
    @POST("/parking-spots/new")
    suspend fun addNewParkingSpot(@Body newParkingSpot: ParkingSpot): Response<Void>

    @GET("/parking-spots")
    suspend fun getParkingSpots(): Response<List<ParkingSpot>>

    @GET("/parking-spots/{parkingId}")
    suspend fun getParkingSpotById(@Path("parkingId") parkingId: Long): Response<ParkingSpot>

    @GET("/parking-spots/available")
    suspend fun getAvailableParkingSpots(@Query("latitude") latitude: Double, @Query("longitude") longitude: Double, @Query("radius") radius: Double): Response<List<ParkingSpot>>

    @GET("/parking-spots/possible")
    suspend fun getPossibleParkingSpots(): Response<List<ParkingSpot>>

    @POST("/parking-spots/check-grpc/{id}")
    suspend fun checkGrpcConnection(@Path("id") id: Long): Response<Void>
}

data class AuthenticateRequest(
    @SerializedName("request_id") val requestId: String,
    val code: String,
    val phoneNumber: String,
)

data class AuthenticateResponse(
    val status: String,
)

data class AuthRequest(
    val phoneNumber: String
)

data class AuthResponse(
    @SerializedName("request_id") val requestId: String
)

data class UserRegistration(
    private val phone: String,
    private val fullName: String,
    private val cars: List<RegisterCar>,
    private val birthday: String,
)

data class RegisterResponse(val token: String)
