package com.project.diaryapp.presentation.diary.model

import android.os.Parcelable
import com.project.diaryapp.data.diary.model.local.DiaryEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Diary(
    var content: String = "",
    var createdAt: String = "",
    var id: String = "",
    var isArchieved: Boolean? = false,
    var title: String = "",
    var updatedAt: String = ""
): Parcelable {
    fun toEntity(): DiaryEntity{
        return DiaryEntity(
            content = content,
            createdAt = createdAt,
            id = id,
            isArchieved = isArchieved,
            title = title,
            updatedAt = updatedAt
        )
    }
}