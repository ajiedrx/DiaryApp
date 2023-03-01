package com.project.diaryapp.data.auth

import com.project.diaryapp.data.auth.model.request.LoginRequest
import com.project.diaryapp.data.auth.model.request.RegisterRequest
import com.project.diaryapp.data.auth.model.response.LoginResponse
import com.project.diaryapp.data.auth.model.response.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}