package com.project.diaryapp.base

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("message")
    val message: String? = ""
)
