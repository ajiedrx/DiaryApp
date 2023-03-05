package com.project.diaryapp.data.diary.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.project.diaryapp.presentation.diary.model.Diary

@Entity
data class DiaryEntity(
    @SerializedName("content")
    val content: String,
    @SerializedName("created_at")
    val createdAt: String,
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("is_archieved")
    val isArchieved: Boolean?,
    @SerializedName("title")
    val title: String,
    @SerializedName("updated_at")
    val updatedAt: String
){
    fun toDomain(): Diary{
        return Diary(
            content = content,
            createdAt = createdAt,
            id = id,
            isArchieved = isArchieved,
            title = title,
            updatedAt = updatedAt
        )
    }
}