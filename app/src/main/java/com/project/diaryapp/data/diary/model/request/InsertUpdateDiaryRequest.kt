package com.project.diaryapp.data.diary.model.request


import com.google.gson.annotations.SerializedName

data class InsertUpdateDiaryRequest(
    @SerializedName("content")
    val content: String,
    @SerializedName("title")
    val title: String
)