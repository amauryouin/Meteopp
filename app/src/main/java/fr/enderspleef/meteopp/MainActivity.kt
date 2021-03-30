package fr.enderspleef.meteopp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import fr.enderspleef.meteopp.data.WeatherModel
import fr.enderspleef.meteopp.data.WeatherSimpleObject
import fr.enderspleef.meteopp.databinding.ActivityMainBinding
import fr.enderspleef.meteopp.time.Time
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

companion object{

    //on prepare une instance de la classe bindée au layout
    private lateinit var binding: ActivityMainBinding

    //ce tag est utilisé partout
    private const val debugTag = "TAG_MESSAGE"

    //ici on déclare notre objet pivot intermédiaire qui va alimenter nos view après avoir récupéré les données de l'API
    private var weatherObject = WeatherSimpleObject()

    //un numéro unique et arbitraire pour la permission de géolocalisation
    private var PERMISSION_ID = 1000

    //on crée une instance de requête de localisation (service google)
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest

}


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test de l'appel de date
        val time: Time = Time()
        time.initialize()

        //on initialise le client de géolocalisation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //on affiche
        binding.homeTodayTextview.text = getString(R.string.home_default_day, time.getTime())
        binding.homeDateTextview.text = getString(R.string.home_default_date, time.getDate())

        //on call les données de manière asynchrone avec enqueue
        OpenWeatherApi().callWeatherData().enqueue(object : Callback<WeatherModel> {
            //dans le cas d'une réponse positive
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    if (!response.isSuccessful) {
                        Log.e(debugTag, "Données non récupérées...")
                    }else {
                        fillWeatherObject(response.body()!!)
                        displayHomeWeather()
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    Log.e(debugTag, "$t")
                }
            })
    }

    //convertit les Kelvins en degrés celsius
    private fun fromKelvinToDegre(tempF: Float): String {
            return ("%.1f".format(tempF - 273.15f) + "°C")
    }

    private fun fromMsToKmh(windMs: Float): String {
        return ("%.1f".format(windMs*3.6f) + "km/h")
    }

    private fun fromBlankToPourcent(humidityB: Float): String {
        return (humidityB.roundToInt()).toString() + "%"
    }

    private fun fillWeatherObject(weatherModel: WeatherModel){
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

    private fun displayHomeWeather(){
        binding.homeRealtimeTemp.setText(fromKelvinToDegre(weatherObject.temp!!))
        binding.homeRealtimeFeltTemp.text = getString(R.string.home_default_realtime_felt_temp, fromKelvinToDegre(weatherObject.feltTemp!!))
        binding.homeWind.setText(fromMsToKmh(weatherObject.windSpeed!!))
        binding.homeHumidity.setText(weatherObject.humidity.toString() + "%")
        binding.homeMaxTemp.setText(fromKelvinToDegre(weatherObject.maxTemp!!))
        binding.homeMinTemp.setText(fromKelvinToDegre(weatherObject.minTemp!!))
    }

    //on vérifie si il y a permission de géolocalisation
    private fun checkPermission():Boolean{
        if(
                  ActivityCompat.checkSelfPermission(
                          this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
               || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED
                ){
            return true
        }
        return false
    }

    //on demande la permission de géolocalisation
    private fun requestPermission(){
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_ID
        )
    }

    //on vérifie si la géolocalisation est activée sur l'appareil
    private fun isLocationEnabled(): Boolean{
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray) {
        //on check les résultats de permission
        //on utilise ce morceau pour débugger
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(debugTag, "You have the permission")
            }
        }
    }

    private fun getLastLocation(){
        //on vérifie la permission
        if(checkPermission()){
            //puis l'activation
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{task ->
                    var location: Location? = task.result
                    if(location == null){

                    }else{
                        Toast.makeText(this, "Vous vous situez aux coordonnées : \nLat:"
                                + location.latitude
                                + " ; Long:" + location.longitude
                                + "\n Ville:" + getCityName(location.latitude, location.longitude)
                                + "\n Pays:" + getCountryName(location.latitude, location.longitude)
                        ,Toast.LENGTH_SHORT)
                    }
                }
            }else{
                Toast.makeText(this, "Veuillez activer la localisation pour bénéficier des infos en temps réel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getNewLocation(){
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 2
        fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest, locationCallback, Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lastLocation: Location = p0.lastLocation
            Toast.makeText(applicationContext, "Vous vous situez aux coordonnées : \nLat:"
                    + lastLocation.latitude
                    + " ; Long:" + lastLocation.longitude
                    + "\n Ville:" + getCityName(lastLocation.latitude, lastLocation.longitude)
                    + "\n Pays:" + getCountryName(lastLocation.latitude, lastLocation.longitude)
            , Toast.LENGTH_SHORT)
        }
    }

    private fun getCityName(lat: Double, long: Double):String{

        var cityName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Address : List<Address> = geoCoder.getFromLocation(lat, long, 1)
        cityName = Address.get(0).locality
        return cityName
    }

    private fun getCountryName(lat: Double, long: Double):String{

        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Address : List<Address> = geoCoder.getFromLocation(lat, long, 1)
        countryName = Address.get(0).countryName
        return countryName
    }
}