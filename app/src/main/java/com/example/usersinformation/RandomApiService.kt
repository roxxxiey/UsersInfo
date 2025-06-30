package com.example.usersinformation

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface RandomApiService {
 @GET("api/")
 suspend fun getUsers(
        @Query("results") count: Int = 10
    ): Response<RandomApiResponse>

    companion object {
         private const val BASE_URL = "https://randomuser.me/"

         fun create(): RandomApiService {
             return Retrofit.Builder()
             .baseUrl(BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
            .build()
             .create(RandomApiService::class.java)
         }
    }
}