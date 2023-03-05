package com.project.diaryapp.data.diary.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.diaryapp.data.diary.model.local.DiaryEntity

@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(film: DiaryEntity) : Long

    @Query("SELECT * from DiaryEntity")
    fun getAll(): List<DiaryEntity>

    @Query("SELECT * from DiaryEntity WHERE id = :id")
    fun get(id: String): DiaryEntity?

    @Query("DELETE FROM DiaryEntity WHERE id = :id")
    fun delete(id: String)

    @Query("DELETE FROM DiaryEntity")
    fun deleteAll()

    @Update
    fun update(data: DiaryEntity) : Int
}