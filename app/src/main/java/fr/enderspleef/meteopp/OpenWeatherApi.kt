package fr.enderspleef.meteopp

import fr.enderspleef.meteopp.OpenWeatherApi.Singleton.api_key
import fr.enderspleef.meteopp.OpenWeatherApi.Singleton.baseUrl
import fr.enderspleef.meteopp.data.WeatherModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OpenWeatherApi(var city: String) {

    object Singleton{
        val baseUrl = "https://api.openweathermap.org/data/2.5/"
        val api_key = "d50a25eab8c0d08e2059faa703d11f94"
    }

    fun callWeatherData(): Call<WeatherModel> {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient.build())
            .build()

        val service = retrofit.create(OpenWeatherService::class.java)
        val weatherCall = service.getCurrentWeather(city, api_key)
        return weatherCall
    }
}