package fr.enderspleef.meteopp.data

import com.squareup.moshi.Json
import java.io.Serializable


class Weather : Serializable {
    @Json(name = "id")
    var id: Int? = null

    @Json(name = "main")
    var main: String? = null

    @Json(name = "description")
    var description: String? = null

    @Json(name = "icon")
    var icon: String? = null

    companion object {
        private const val serialVersionUID = 24764444918512027L
    }
}