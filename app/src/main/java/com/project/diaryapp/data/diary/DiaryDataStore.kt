package com.project.diaryapp.data.diary

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import com.project.diaryapp.data.lib.handleApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DiaryDataStore(private val diaryApiService: DiaryApiService) : DiaryRepository {
    override fun createDiary(request: InsertUpdateDiaryRequest): Flow<DiaryResponse> {
        return flow {
            emit(diaryApiService.createDiary(request).handleApiError())
        }
    }

    override fun getDiaryList(): Flow<PagingData<DiaryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                DiaryPagingSource(diaryApiService)
            }
        ).flow
    }

    override fun getDiary(diaryID: String): Flow<DiaryResponse> {
        return flow {
            emit(diaryApiService.getDiary(diaryID).handleApiError())
        }
    }

    override fun updateDiary(
        diaryID: String,
        request: InsertUpdateDiaryRequest
    ): Flow<DiaryResponse> {
        return flow {
            emit(diaryApiService.updateDiary(diaryID, request).handleApiError())
        }
    }

    override fun archiveDiary(diaryID: String): Flow<DiaryResponse> {
        return flow {
            emit(diaryApiService.archiveDiary(diaryID).handleApiError())
        }
    }

    override fun unarchiveDiary(diaryID: String): Flow<DiaryResponse> {
        return flow {
            emit(diaryApiService.unarchiveDiary(diaryID).handleApiError())
        }
    }
}