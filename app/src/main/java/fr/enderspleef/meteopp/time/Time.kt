package fr.enderspleef.meteopp.time

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class Time() {

    lateinit var rawTime: LocalDateTime
    lateinit var formattedTime: String
    lateinit var formattedDate: String
    lateinit var formattedDateTime: String
    lateinit var month: String


    @RequiresApi(Build.VERSION_CODES.O)
    fun initialize() {
        //récupère le temps sous un format primaire
        rawTime = LocalDateTime.now()
        //crée 3 formateurs de date/time
        val timeFormatter = DateTimeFormatter.ofPattern("H:mm")
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/y")
        val dateTimeFormatter = DateTimeFormatter.RFC_1123_DATE_TIME
        //adapte le temps selon un formatteur
        formattedTime = rawTime.format(timeFormatter)
        formattedDate = rawTime.format(dateFormatter)
        Log.d("DATEFORMAT", formattedTime)
    }

    //récupère le mois en littéral
    fun getWholeMonth(){
        //initialise le mois en francais à partir de son annotation anglaise
        month = with(formattedDateTime) {
            when {
                contains("Jan") -> Month.Jan.monthFr
                contains("Feb") -> Month.Feb.monthFr
                contains("Mar") -> Month.Mar.monthFr
                contains("Apr") -> Month.Apr.monthFr
                contains("May") -> Month.May.monthFr
                contains("Jun") -> Month.Jun.monthFr
                contains("Jul") -> Month.Jul.monthFr
                contains("Aug") -> Month.Aug.monthFr
                contains("Sep") -> Month.Sep.monthFr
                contains("Oct") -> Month.Oct.monthFr
                contains("Nov") -> Month.Nov.monthFr
                contains("Dec") -> Month.Dec.monthFr
                else -> return
            }
        }
    }
    //retourne l'heure en propre
    fun getTime(): String{
        return formattedTime
    }
    //retourne la date en propre
    fun getDate(): String{
        return formattedDate
    }
}