package fr.enderspleef.meteopp.data

data class WeatherSimpleObject(
    var id: String? = "",
    var main: String? = "",
    var temp: Float? = null,
    var feltTemp: Float? = null,
    var maxTemp: Float? = null,
    var minTemp: Float? = null,
    var windSpeed: Float? = null,
    var humidity: Float? = null
)