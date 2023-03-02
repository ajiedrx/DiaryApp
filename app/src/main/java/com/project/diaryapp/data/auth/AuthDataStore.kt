package com.project.diaryapp.data.auth

import android.content.SharedPreferences
import com.project.diaryapp.Const
import com.project.diaryapp.data.auth.model.request.LoginRequest
import com.project.diaryapp.data.auth.model.request.RegisterRequest
import com.project.diaryapp.data.auth.model.response.LoginResponse
import com.project.diaryapp.data.auth.model.response.RegisterResponse
import com.project.diaryapp.data.lib.handleApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthDataStore(private val authApiService: AuthApiService, private val sharedPreferences: SharedPreferences.Editor): AuthRepository {
    override fun register(request: RegisterRequest): Flow<RegisterResponse>{
        return flow {
            emit(authApiService.register(request).handleApiError())
        }
    }

    override fun login(request: LoginRequest): Flow<LoginResponse>{
        return flow {
            val response = authApiService.login(request).handleApiError()
            sharedPreferences.putString(Const.USER_TOKEN, response.accessToken).apply()
            emit(response)
        }
    }
}