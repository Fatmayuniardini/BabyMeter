package com.capstone.babymeter.network

import com.capstone.babymeter.model.request.LoginRequest
import com.capstone.babymeter.model.request.RegisterRequest
import com.capstone.babymeter.model.response.AuthResponse
import com.capstone.babymeter.model.response.DeletePredictionResponse
import com.capstone.babymeter.model.response.LogoutResponse
import com.capstone.babymeter.model.response.PredictionResponse
import com.capstone.babymeter.model.response.PredictionsResponse
import com.capstone.babymeter.model.request.ModifyPredicitionRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiService {
    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("/auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<LogoutResponse>

    @POST("/nurse/predictions")
    suspend fun createPrediction(
        @Header("Authorization") token: String,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>

    @GET("/nurse/predictions")
    suspend fun getPredictions(@Header("Authorization") token: String): Response<PredictionsResponse>

    @PUT("/nurse/predictions/{id}")
    suspend fun modifyPrediction(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: ModifyPredicitionRequest
    ): Response<PredictionResponse>

    @GET("/nurse/predictions/{id}")
    suspend fun getPredictionById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<PredictionResponse>

    @DELETE("/nurse/predictions/{id}")
    suspend fun deletePrediction(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<DeletePredictionResponse>
}