package com.example.aparking.authorization

import com.example.aparking.User
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {
    @POST("/auth/request")
    fun sendAuthRequest(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("/auth/verify")
    fun verifyCode(@Body request: VerificationRequest): Call<LoginResponse>

    @POST("/auth/register")
    fun registerUser(@Body userRegistration: UserRegistration): Call<RegisterResponse>

    @GET("/users/byPhone")
    fun getUserByPhone(@Query("phoneNumber") phoneNumber: String): Call<User>
}

data class PhoneNumber(val phoneNumber: String)
data class RequestResponse(val requestId: String)
data class AuthRequest(
    val phoneNumber: String
)

data class AuthResponse(
    @SerializedName("request_id") val requestId: String
)

data class VerificationRequest(
    val requestId: String,
    val code: String,
    val phoneNumber: String
)

data class VerificationResponse(
    @SerializedName("token") val token: String
)

data class LoginResponse(
    val token: String,
    val status: String,
    val phoneNumber: String?  // phoneNumber будет null, если status == "login"
)

data class RegistrationResponse(val phoneNumber: String, val status: String)

data class UserRegistration(val name: String, val phoneNumber: String)
data class RegisterResponse(val token: String)
