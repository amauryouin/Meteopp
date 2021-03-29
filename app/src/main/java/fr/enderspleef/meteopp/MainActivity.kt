package fr.enderspleef.meteopp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import fr.enderspleef.meteopp.data.WeatherModel
import fr.enderspleef.meteopp.data.WeatherSimpleObject
import fr.enderspleef.meteopp.databinding.ActivityMainBinding
import fr.enderspleef.meteopp.time.Time
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

companion object{

    //on prepare une instance de la classe bindée au layout
    private lateinit var binding: ActivityMainBinding

    //ce tag est utilisé partout
    private const val debugTag = "TAG_MESSAGE"

    //ici on déclare notre objet pivot intermédiaire qui va alimenter nos view après avoir récupéré les données de l'API
    private var weatherObject = WeatherSimpleObject()

}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test de l'appel de date
        val time: Time = Time()
        time.initialize()
        binding.homeTodayTextview.text = getString(R.string.home_default_day, time.getTime())
        binding.homeDateTextview.text = getString(R.string.home_default_date, time.getDate())

        OpenWeatherApi().callWeatherData().enqueue(object : Callback<WeatherModel> {
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(debugTag, "Données non récupérées...")
                    }
                    fillWeatherObject(response.body()!!)
                    displayHomeWeather()
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Log.e(debugTag, "$t")
                }
            })
    }

    //convertit les Kelvins en degrés celsius
    fun fromKelvinToDegre(tempF: Float): String {
            return ("%.1f".format(tempF - 273.15f) + "°C")
    }

    fun fromMsToKmh(windMs: Float): String {
        return ("%.1f".format(windMs*3.6f) + "km/h")
    }

    fun fromBlankToPourcent(humidityB: Float): String {
        return (humidityB.roundToInt()).toString() + "%"
    }

    fun fillWeatherObject(weatherModel: WeatherModel){
        try {
            //on remplit notre objet réel avec les données du POJO
            weatherObject.temp = weatherModel.main!!.temp?.toFloat()
            weatherObject.feltTemp = weatherModel.main!!.feels_like?.toFloat()
            weatherObject.main = weatherModel.weather!![0].main
            weatherObject.maxTemp = weatherModel.main!!.temp_max?.toFloat()
            weatherObject.minTemp = weatherModel.main!!.temp_min?.toFloat()
            weatherObject.windSpeed = weatherModel.wind!!.speed?.toFloat()
            weatherObject.humidity = weatherModel.main!!.humidity?.toFloat()
        }catch (e: UninitializedPropertyAccessException){
            Log.e(debugTag, "L'objet pivot de type WeatherSimpleobject n'a pas été initialisé\n$e")
        }
    }

    fun displayHomeWeather(){
        binding.homeRealtimeTemp.setText(fromKelvinToDegre(weatherObject.temp!!))
        binding.homeRealtimeFeltTemp.text = getString(R.string.home_default_realtime_felt_temp, fromKelvinToDegre(weatherObject.feltTemp!!))
        binding.homeWind.setText(fromMsToKmh(weatherObject.windSpeed!!))
        binding.homeHumidity.setText(weatherObject.humidity.toString() + "%")
        binding.homeMaxTemp.setText(fromKelvinToDegre(weatherObject.maxTemp!!))
        binding.homeMinTemp.setText(fromKelvinToDegre(weatherObject.minTemp!!))
    }
}