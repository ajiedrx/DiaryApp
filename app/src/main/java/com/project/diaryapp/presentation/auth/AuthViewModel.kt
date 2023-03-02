package com.project.diaryapp.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.diaryapp.base.Result
import com.project.diaryapp.data.auth.AuthRepository
import com.project.diaryapp.data.auth.model.request.LoginRequest
import com.project.diaryapp.data.auth.model.request.RegisterRequest
import com.project.diaryapp.data.auth.model.response.LoginResponse
import com.project.diaryapp.data.auth.model.response.RegisterResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _login = MutableLiveData<Result<LoginResponse>>()
    val login: LiveData<Result<LoginResponse>> by lazy { _login }

    fun login(email: String, password: String){
        viewModelScope.launch {
            authRepository
                .login(
                    LoginRequest(email, password)
                )
                .onStart {
                    _login.value = Result.Loading
                }
                .catch {
                    _login.value = Result.Error(it.message.orEmpty())
                }
                .collect{
                    _login.value = Result.Success(it)
                }
        }
    }

    private var _register = MutableLiveData<Result<RegisterResponse>>()
    val register: LiveData<Result<RegisterResponse>> by lazy { _register }

    fun register(email: String, password: String, username: String){
        viewModelScope.launch {
            authRepository
                .register(
                    RegisterRequest(email, password, password, username)
                )
                .onStart {
                    _register.value = Result.Loading
                }
                .catch {
                    _register.value = Result.Error(it.message.orEmpty())
                }
                .collect{
                    _register.value = Result.Success(it)
                }
        }
    }
}