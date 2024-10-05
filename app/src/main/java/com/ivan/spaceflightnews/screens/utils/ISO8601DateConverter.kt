package com.ivan.spaceflightnews.screens.utils

import java.text.SimpleDateFormat
import java.util.Locale

object ISO8601DateConverter {

    private val iso8601DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

    fun convertISO8601DateToOtherFormat(iso8601DateString: String, otherFormat: SimpleDateFormat): String {
        val date = iso8601DateFormat.parse(iso8601DateString)
        return date?.let { otherFormat.format(it) } ?: ""
    }

}