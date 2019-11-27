/*
 * Copyright (c) DAR Ecosystem 2018.
 *
 * Created by sough on 10/09/2018.
 */

package com.example.letsplay.helper

import com.example.letsplay.service.LocalStorage
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

/**
 * This class handle current user authorization status
 */
object LanguageManager: KoinComponent {

    private val localStorage: LocalStorage by inject()

    fun getToken(): String? = localStorage.getToken()

    fun getLanguage() : String {
        return when(Locale.getDefault().language){
            "in" -> "id"
            else -> "en-EN"
        }
    }
}