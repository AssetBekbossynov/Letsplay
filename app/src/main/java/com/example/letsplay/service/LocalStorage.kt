package com.example.letsplay.service

interface LocalStorage {
    fun setToken(token: String)
    fun getToken(): String?
    fun wipeToken()
}