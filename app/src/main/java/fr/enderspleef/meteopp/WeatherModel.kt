package fr.enderspleef.meteopp

import com.squareup.moshi.Json
import fr.enderspleef.meteopp.data.*
import java.io.Serializable

class WeatherModel : Serializable {
    @Json(name = "coord")
    var coord: Coord? = null

    @Json(name = "weather")
    var weather: List<Weather>? = null

    @Json(name = "base")
    var base: String? = null

    @Json(name = "main")
    var main: Main? = null

    @Json(name = "visibility")
    var visibility: Int? = null

    @Json(name = "wind")
    var wind: Wind? = null

    @Json(name = "clouds")
    var clouds: Clouds? = null

    @Json(name = "dt")
    var dt: Int? = null

    @Json(name = "sys")
    var sys: Sys? = null

    @Json(name = "timezone")
    var timezone: Int? = null

    @Json(name = "id")
    var id: Int? = null

    @Json(name = "name")
    var name: String? = null

    @Json(name = "cod")
    var cod: Int? = null

    companion object {
        private const val serialVersionUID = 6701426417345430031L
    }
}



