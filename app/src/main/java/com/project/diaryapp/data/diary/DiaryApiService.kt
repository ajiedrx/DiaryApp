package com.project.diaryapp.data.diary

import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import com.project.diaryapp.data.diary.model.response.ListDiaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DiaryApiService {
    @POST("/diary")
    @Headers("Authorization:need")
    suspend fun createDiary(@Body request: InsertUpdateDiaryRequest): Response<DiaryResponse>

    @GET("/diary")
    @Headers("Authorization:need")
    suspend fun getDiaryList(@Query("page") page: Int, @Query("search") searchParam: String?): Response<ListDiaryResponse>

    @GET("/diary")
    @Headers("Authorization:need")
    suspend fun getDiary(@Query("diary_id") diaryID: String): Response<DiaryResponse>

    @PUT("/diary/{diary_id}")
    @Headers("Authorization:need")
    suspend fun updateDiary(@Path("diary_id") diaryID: String, @Body request: InsertUpdateDiaryRequest): Response<DiaryResponse>

    @PUT("/diary/{diary_id}/archieve")
    @Headers("Authorization:need")
    suspend fun archiveDiary(@Path("diary_id") diaryID: String): Response<DiaryResponse>

    @PUT("/diary/{diary_id}/unarchieve")
    @Headers("Authorization:need")
    suspend fun unarchiveDiary(@Path("diary_id") diaryID: String): Response<DiaryResponse>
}
