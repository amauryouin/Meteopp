package fr.enderspleef.meteopp.data

import com.squareup.moshi.Json
import java.io.Serializable


class Sys : Serializable {
    @Json(name = "type")
    var type: Int? = null

    @Json(name = "id")
    var id: Int? = null

    @Json(name = "country")
    var country: String? = null

    @Json(name = "sunrise")
    var sunrise: Int? = null

    @Json(name = "sunset")
    var sunset: Int? = null

    companion object {
        private const val serialVersionUID = -386761585018335847L
    }
}