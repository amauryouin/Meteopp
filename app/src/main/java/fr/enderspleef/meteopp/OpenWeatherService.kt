package fr.enderspleef.meteopp

import fr.enderspleef.meteopp.data.WeatherModel
import retrofit2.Call
import retrofit2.http.*

interface OpenWeatherService {

    @GET("weather/?")
    fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") api_key: String
    ): Call<WeatherModel>
}