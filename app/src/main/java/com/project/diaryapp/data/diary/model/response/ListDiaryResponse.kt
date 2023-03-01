package com.project.diaryapp.data.diary.model.response


import com.google.gson.annotations.SerializedName
import com.project.diaryapp.base.BaseResponse

data class ListDiaryResponse(
    @SerializedName("data")
    val data: List<DiaryResponse>?
) : BaseResponse()
