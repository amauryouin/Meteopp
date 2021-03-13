package fr.enderspleef.meteopp.data

import com.squareup.moshi.Json
import java.io.Serializable

class Wind : Serializable {
    @Json(name = "speed")
    var speed: Double? = null

    @Json(name = "deg")
    var deg: Int? = null

    companion object {
        private const val serialVersionUID = 3081391021245156333L
    }
}