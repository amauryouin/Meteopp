package fr.enderspleef.meteopp.data

import com.squareup.moshi.Json
import java.io.Serializable


class Coord : Serializable {
    @Json(name = "lon")
    var lon: Double? = null

    @Json(name = "lat")
    var lat: Double? = null

    companion object {
        private const val serialVersionUID = -7808944357346932318L
    }
}