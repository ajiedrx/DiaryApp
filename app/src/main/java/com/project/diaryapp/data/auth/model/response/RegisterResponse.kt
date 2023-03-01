package com.project.diaryapp.data.auth.model.response


import com.google.gson.annotations.SerializedName
import com.project.diaryapp.base.BaseResponse

data class RegisterResponse(
    @SerializedName("email")
    val email: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("username")
    val username: String?
) : BaseResponse()