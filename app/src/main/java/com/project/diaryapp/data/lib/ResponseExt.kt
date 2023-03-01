package com.project.diaryapp.data.lib

import com.google.gson.Gson
import com.project.diaryapp.base.BaseResponse
import retrofit2.Response

fun <T> Response<T>.handleApiError(): T =
    try {
        if(!isSuccessful){
            val errorBody = Gson().fromJson(errorBody()?.string(), BaseResponse::class.java)
            throw Exception(errorBody?.message)
        } else {
            body()!!
        }
    } catch (e: ClassCastException){
        when{
            !isSuccessful -> throw Exception(this.message())
            else -> body() ?: kotlin.run {
                throw java.lang.Exception(this.message())
            }
        }
    }