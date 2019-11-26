/*
 * Copyright (c) DAR Ecosystem 2018.
 *
 * Created by sough on 10/09/2018.
 */

package com.example.letsplay.helper

import java.util.*

/**
 * This class handle current user authorization status
 */
object LanguageManager {

    fun getLanguage() : String {
        return when(Locale.getDefault().language){
            "in" -> "id"
            else -> "en-EN"
        }
    }
}