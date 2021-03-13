package fr.enderspleef.meteopp.data

import com.squareup.moshi.Json
import java.io.Serializable


class Clouds : Serializable {
    @Json(name = "all")
    var all: Int? = null

    companion object {
        private const val serialVersionUID = 8635743523047716325L
    }
}