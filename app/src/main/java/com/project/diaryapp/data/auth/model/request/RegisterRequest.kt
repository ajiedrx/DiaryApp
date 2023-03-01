package com.project.diaryapp.data.auth.model.request


import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("email")
    val email: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("password_confirmation")
    val passwordConfirmation: String?,
    @SerializedName("username")
    val username: String?
)