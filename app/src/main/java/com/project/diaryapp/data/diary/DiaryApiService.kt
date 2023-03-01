package com.project.diaryapp.data.diary

import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import com.project.diaryapp.data.diary.model.response.ListDiaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaryApiService {
    @POST("/diary")
    suspend fun createDiary(@Body request: InsertUpdateDiaryRequest): Response<DiaryResponse>

    @GET("/diary")
    suspend fun getDiaryList(@Query("page") page: Int): Response<ListDiaryResponse>

    @GET("/diary")
    suspend fun getDiary(@Query("diary_id") diaryID: String): Response<DiaryResponse>

    @PUT("/diary")
    suspend fun updateDiary(@Query("diary_id") diaryID: String, @Body request: InsertUpdateDiaryRequest): Response<DiaryResponse>

    @PUT("/diary/{diary_id}/archieve")
    suspend fun archiveDiary(@Path("diary_id") diaryID: String): Response<DiaryResponse>

    @PUT("/diary/{diary_id}/unarchieve")
    suspend fun unarchiveDiary(@Path("diary_id") diaryID: String): Response<DiaryResponse>
}
