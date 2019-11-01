package com.example.letsplay.enitity

import com.google.gson.annotations.SerializedName

data class ResponseError(@SerializedName("debugMessage") val debugMessage: String?,
                         @SerializedName("message") val message: String?,
                         @SerializedName("path") val path: String?,
                         @SerializedName("status") val status: Int?,
                         @SerializedName("validationError") val validationErrors: List<CustomError>?)
/*
{
    "debugMessage": "string",
    "message": "string",
    "path": "string",
    "status": 0,
    "timestamp": "string",
    "validationErrors": [
    {
        "field": "string",
        "message": "string",
        "object": "string",
        "rejectedValue": {}
    }
    ]
}*/