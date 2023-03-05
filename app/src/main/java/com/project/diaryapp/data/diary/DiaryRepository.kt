package com.project.diaryapp.data.diary

import androidx.paging.PagingData
import com.project.diaryapp.data.diary.model.local.DiaryEntity
import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import kotlinx.coroutines.flow.Flow

interface DiaryRepository {
    fun createDiary(request: InsertUpdateDiaryRequest): Flow<DiaryResponse>
    fun getDiaryList(page: Int, query: String?): Flow<PagingData<DiaryResponse>>
    fun getDiary(diaryID: String): Flow<DiaryResponse>
    fun updateDiary(diaryID: String, request: InsertUpdateDiaryRequest): Flow<Boolean>
    fun archiveDiary(diary: DiaryEntity): Flow<DiaryResponse>
    fun unarchiveDiary(diaryID: String): Flow<DiaryResponse>
    fun getArchivedDiary(): Flow<List<DiaryEntity>>
}