/*
 * Copyright (c) DAR Ecosystem 2018.
 *
 * Created by sough on 10/07/2018.
 */

package com.example.letsplay.enitity.exception

import com.example.letsplay.enitity.ResponseError
import com.google.gson.stream.MalformedJsonException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class RetrofitException internal constructor(val exMessage: String?,
                                             /** The request URL which produced the error.  */
                                             val url: String?,
                                             /** Response object containing status code, headers, body, etc.  */
                                             val response: Response<*>?,
                                             /** The event kind which triggered this error.  */
                                             val kind: Kind,
                                             exception: Throwable?,
                                             /** The Retrofit this request was executed on  */
                                             private val retrofit: Retrofit?) : RuntimeException(exMessage, exception) {

    /** Identifies the event kind which triggered a [RetrofitException].  */
    enum class Kind {
        /** An [IOException] occurred while communicating to the server.  */
        NETWORK,
        /** A non-200 HTTP status code was received from the server.  */
        HTTP,
        /**
         * An internal error occurred while attempting to execute a request. It is best practice to
         * re-throw this exception so your application crashes.
         */
        UNEXPECTED
    }

    var responseError: ResponseError? = null

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun <T> getErrorBodyAs(type: Class<T>): T? {
        if (response?.errorBody() == null) {
            return null
        }
        val converter = retrofit?.responseBodyConverter<T>(type, arrayOfNulls(0))
        return converter?.convert(response.errorBody()!!)
    }

    /**
     * HTTP response body converted to specified `type`. `null` if there is no
     * response.
     *
     * @throws IOException if unable to convert the body to the specified `type`.
     */
    @Throws(IOException::class)
    fun getErrorBody(): ResponseError? {
        if (responseError != null) {
            return responseError
        }

        if (response?.errorBody() == null) {
            responseError = if (exMessage == null) {
                ResponseError("error", "Server Error")
            } else {
                ResponseError("error", exMessage)
            }
            return responseError
        }
        val converter = retrofit?.responseBodyConverter<ResponseError>(ResponseError::class.java, arrayOfNulls(0))
        try {
            responseError = converter?.convert(response.errorBody()!!)
        } catch (e: MalformedJsonException) {
            return ResponseError("error", "Server Error")
        }

        return responseError
    }

    fun getCode() = response?.code()

    fun getErrorMessage() = getErrorBody()?.message

    companion object {
        fun httpError(url: String, response: Response<*>, retrofit: Retrofit): RetrofitException {
            val message = response.code().toString() + " " + response.message()
            return RetrofitException(message, url, response, Kind.HTTP, null, retrofit)
        }

        fun networkError(exception: IOException): RetrofitException {
            return RetrofitException(exception.message!!, null, null, Kind.NETWORK, exception, null)
        }

        fun unexpectedError(exception: Throwable): RetrofitException {
            return RetrofitException(exception.message!!, null, null, Kind.UNEXPECTED, exception, null)
        }
    }
}