package fr.enderspleef.meteopp

import retrofit2.Call
import retrofit2.http.*

interface OpenWeatherService {

    @GET("weather/?")
    fun getCurrentTemp(
        @Query("q") city: String,
        @Query("appid") api_key: String
    ): Call<WeatherModel>
}