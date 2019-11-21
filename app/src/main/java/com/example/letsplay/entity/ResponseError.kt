package com.example.letsplay.entity

import com.google.gson.annotations.SerializedName

data class ResponseError(@SerializedName("message") val message: String?,
                         @SerializedName("status") val status: Int?)
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