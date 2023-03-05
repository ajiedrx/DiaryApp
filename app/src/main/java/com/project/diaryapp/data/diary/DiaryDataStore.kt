package com.project.diaryapp.data.diary

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.project.diaryapp.data.diary.model.DiaryDao
import com.project.diaryapp.data.diary.model.local.DiaryEntity
import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import com.project.diaryapp.data.lib.handleApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DiaryDataStore(private val diaryApiService: DiaryApiService, private val diaryDao: DiaryDao) : DiaryRepository {
    override fun createDiary(request: InsertUpdateDiaryRequest): Flow<DiaryResponse> {
        return flow {
            emit(diaryApiService.createDiary(request).handleApiError())
        }
    }

    override fun getDiaryList(page: Int, query: String?): Flow<PagingData<DiaryResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                DiaryPagingSource(diaryApiService, query)
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
    ): Flow<Boolean> {
        return flow {
            val localData = diaryDao.get(diaryID)
            localData?.let {
                diaryDao.update(
                    it.copy(
                        title = request.title,
                        content = request.content
                    )
                )
            }
            if(localData == null){
                diaryApiService.updateDiary(diaryID, request).handleApiError()
            }
            emit(true)
        }
    }

    override fun archiveDiary(diary: DiaryEntity): Flow<DiaryResponse> {
        return flow {
            diaryDao.insert(diary)
            emit(diaryApiService.archiveDiary(diary.id).handleApiError())
        }
    }

    override fun unarchiveDiary(diaryID: String): Flow<DiaryResponse> {
        return flow {
            diaryDao.delete(diaryID)
            emit(diaryApiService.unarchiveDiary(diaryID).handleApiError())
        }
    }

    override fun getArchivedDiary(): Flow<List<DiaryEntity>> {
        return flow {
            emit(diaryDao.getAll())
        }
    }
}