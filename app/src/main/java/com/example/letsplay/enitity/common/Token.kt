package com.example.letsplay.enitity.common

import com.google.gson.annotations.SerializedName

const val TD_TOKEN = "token"
const val TD_REFRESH_TOKEN = "refresh_token"
const val TD_TOKEN_TYPE = "token_type"
const val TD_SCOPE = "scope"
const val TD_EXPIRES_ID = "expires_in"

data class Token(@SerializedName(TD_TOKEN) val token: String)