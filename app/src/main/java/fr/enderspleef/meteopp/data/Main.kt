package fr.enderspleef.meteopp.data

import com.squareup.moshi.Json
import java.io.Serializable


class Main : Serializable {
    @Json(name = "temp")
    var temp: Double? = null

    @Json(name = "feels_like")
    var feels_like: Double? = null

    @Json(name = "temp_min")
    var temp_min: Double? = null

    @Json(name = "temp_max")
    var temp_max: Double? = null

    @Json(name = "pressure")
    var pressure: Int? = null

    @Json(name = "humidity")
    var humidity: Int? = null

    companion object {
        private const val serialVersionUID = -5561792976096903088L
    }
}
