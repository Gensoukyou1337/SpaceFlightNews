package com.ivan.spaceflightnews.screens.utils

import android.content.Context
import java.util.Calendar

object GreetingParser {
    fun getGreeting(context: Context, currentHourIn24Format: Int, userName: String): String {
        val greetingFirstPart = context.getString(
            if (currentHourIn24Format < 6) {
                com.ivan.spaceflightnews.R.string.night
            } else if (currentHourIn24Format < 12) {
                com.ivan.spaceflightnews.R.string.morning
            } else if (currentHourIn24Format < 16) {
                com.ivan.spaceflightnews.R.string.afternoon
            } else if (currentHourIn24Format < 19) {
                com.ivan.spaceflightnews.R.string.evening
            } else {
                com.ivan.spaceflightnews.R.string.night
            }
        )

        return context.getString(com.ivan.spaceflightnews.R.string.greeting, greetingFirstPart, userName)
    }
}