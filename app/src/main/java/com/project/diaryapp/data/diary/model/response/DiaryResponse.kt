package com.project.diaryapp.data.diary.model.response


import com.google.gson.annotations.SerializedName
import com.project.diaryapp.base.BaseResponse
import com.project.diaryapp.presentation.diary.model.Diary

data class DiaryResponse(
    @SerializedName("content")
    val content: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("is_archieved")
    val isArchieved: Boolean?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?
): BaseResponse() {
    fun toDomain(): Diary{
        return Diary(
            content = content.orEmpty(),
            createdAt = createdAt.orEmpty(),
            id = id.orEmpty(),
            isArchieved = isArchieved,
            title = title.orEmpty(),
            updatedAt = updatedAt.orEmpty()
        )
    }
}