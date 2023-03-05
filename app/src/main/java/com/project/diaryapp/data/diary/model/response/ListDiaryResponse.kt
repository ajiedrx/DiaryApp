package com.project.diaryapp.data.diary.model.response


import com.google.gson.annotations.SerializedName
import com.project.diaryapp.base.BaseResponse

data class ListDiaryResponse(
    @SerializedName("data")
    val data: List<DiaryResponse>?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("total_data")
    val total_data: Int?
) : BaseResponse()
