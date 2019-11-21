package com.example.letsplay.helper

import com.example.letsplay.entity.ResponseError

sealed class UseCaseResult<out T : Any> {
    class Success<out T : Any>(val data: T) : UseCaseResult<T>()
    class Error(val exception: Throwable? = null, val error: ResponseError? = null) : UseCaseResult<Nothing>()
}