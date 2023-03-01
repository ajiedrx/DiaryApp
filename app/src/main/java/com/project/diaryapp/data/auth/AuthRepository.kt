package com.project.diaryapp.data.auth

import com.project.diaryapp.data.auth.model.request.LoginRequest
import com.project.diaryapp.data.auth.model.request.RegisterRequest
import com.project.diaryapp.data.auth.model.response.LoginResponse
import com.project.diaryapp.data.auth.model.response.RegisterResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(request: RegisterRequest): Flow<RegisterResponse>
    fun login(request: LoginRequest): Flow<LoginResponse>
}