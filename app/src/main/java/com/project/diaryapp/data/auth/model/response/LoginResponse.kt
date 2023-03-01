package com.project.diaryapp.data.auth.model.response


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("user")
    val user: User?
) {
    data class User(
        @SerializedName("email")
        val email: String?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("username")
        val username: String?
    )
}