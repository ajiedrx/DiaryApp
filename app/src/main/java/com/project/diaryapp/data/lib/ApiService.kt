package com.project.diaryapp.data.lib

import retrofit2.Retrofit

object ApiService {
    fun <T> createReactiveService(
        serviceClass: Class<T>,
        retrofitClient: Retrofit
    ): T {
        return retrofitClient.create(serviceClass)
    }
}