package com.project.diaryapp.data.diary

import androidx.paging.PagingData
import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun createDiary(request: InsertUpdateDiaryRequest): Flow<DiaryResponse>
    fun getDiaryList(): Flow<PagingData<DiaryResponse>>
    fun getDiary(diaryID: String): Flow<DiaryResponse>
    fun updateDiary(diaryID: String, request: InsertUpdateDiaryRequest): Flow<DiaryResponse>
    fun archiveDiary(diaryID: String): Flow<DiaryResponse>
    fun unarchiveDiary(diaryID: String): Flow<DiaryResponse>
}