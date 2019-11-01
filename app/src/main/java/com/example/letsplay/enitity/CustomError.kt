package com.example.letsplay.enitity

import com.google.gson.annotations.SerializedName

data class CustomError (@SerializedName("field") val field: String,
                        @SerializedName("message") val message: String,
                        @SerializedName("object") val someObject: String){
}

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