package com.example.weatherapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import weatherapp

interface ApiInterface {
    @GET("weather")
    fun getWeather(
        @Query("q") city: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<weatherapp>
}