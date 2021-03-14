package fr.enderspleef.meteopp

import android.util.Log
import fr.enderspleef.meteopp.Singleton.api_key
import fr.enderspleef.meteopp.Singleton.baseUrl
import fr.enderspleef.meteopp.Singleton.localCity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Singleton{
    val baseUrl = "https://api.openweathermap.org/data/2.5/"
    val localCity = "Paris"
    val api_key = "d50a25eab8c0d08e2059faa703d11f94"
}

class OpenWeatherApi {
    val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(httpClient.build())
        .build()

    val service = retrofit.create(OpenWeatherService::class.java)
    val temp = service.getCurrentTemp(localCity, api_key)

    fun fetchWeather() {

        temp.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (response.isSuccessful) {
                    Log.d("RESULTAT", response.body().toString())
                } else {
                    return
                }
            }
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.e("RESULTAT", "$t")
            }
        })
    }
}