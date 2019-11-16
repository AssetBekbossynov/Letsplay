/*
 * Copyright (c) DAR Ecosystem 2018.
 *
 * Created by sough on 10/09/2018.
 */

package com.example.letsplay.repository

import android.content.SharedPreferences
import com.example.letsplay.enitity.common.TD_TOKEN
import com.example.letsplay.service.LocalStorage

/**
 * Handle local Auth storage
 */
class PrefsAuthDataStore(private val prefs: SharedPreferences) : LocalStorage {
    override fun getToken(): String? {
        return prefs.getString(TD_TOKEN, null)
    }

    override fun setToken(token: String) {
        prefs.edit().putString(TD_TOKEN, token).apply()
    }

    override fun wipeToken() {
        prefs.edit().putString(TD_TOKEN, null).apply()
    }
}