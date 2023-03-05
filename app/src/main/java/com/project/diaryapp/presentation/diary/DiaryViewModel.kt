package com.project.diaryapp.presentation.diary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.map
import com.project.diaryapp.base.Result
import com.project.diaryapp.data.diary.DiaryRepository
import com.project.diaryapp.data.diary.model.request.InsertUpdateDiaryRequest
import com.project.diaryapp.data.diary.model.response.DiaryResponse
import com.project.diaryapp.presentation.SingleLiveEvent
import com.project.diaryapp.presentation.diary.model.Diary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DiaryViewModel(private val diaryRepository: DiaryRepository): ViewModel() {

    private var  _createDiary = MutableLiveData<Result<DiaryResponse>>()
    val createDiary: LiveData<Result<DiaryResponse>> by lazy { _createDiary }

    fun createDiary(title: String, content: String){
        viewModelScope.launch {
            diaryRepository
                .createDiary(
                    InsertUpdateDiaryRequest(title = title, content = content)
                )
                .onStart {
                    _createDiary.value = Result.Loading
                }
                .catch {
                    _createDiary.value = Result.Error(it.message.orEmpty())
                }
                .collect{
                    _createDiary.value = Result.Success(it)
                }
        }
    }

    private val _diaryList = MutableLiveData<Result<PagingData<Diary>>>()
    val diaryList: LiveData<Result<PagingData<Diary>>> by lazy { _diaryList }

    fun getAllDiaryList(query: String? = null){
        viewModelScope.launch {
            diaryRepository
                .getDiaryList(1, query)
                .onStart {
                    _diaryList.value = Result.Loading
                }
                .catch {
                    _diaryList.value = Result.Error(it.message.orEmpty())
                }
                .collectLatest{
                    _diaryList.value = Result.Success(it.map { it.toDomain() })
                }
        }
    }

    private val _archivedDiary = MutableLiveData<Result<List<Diary>>>()
    val archivedDiary: LiveData<Result<List<Diary>>> by lazy { _archivedDiary }

    fun getArchivedDiary(){
        viewModelScope.launch {
            diaryRepository
                .getArchivedDiary()
                .onStart {
                    _archivedDiary.value = Result.Loading
                }
                .catch {
                    _archivedDiary.value = Result.Error(it.message.orEmpty())
                }
                .collectLatest{
                    _archivedDiary.value = Result.Success(it.map { it.toDomain() })
                }
        }
    }

    private val _archiveDiary = SingleLiveEvent<Result<Diary>>()
    val archiveDiary: LiveData<Result<Diary>> by lazy { _archiveDiary }

    fun archiveDiary(diary: Diary){
        viewModelScope.launch {
            diaryRepository
                .archiveDiary(diary.toEntity())
                .onStart {
                    _archiveDiary.value = Result.Loading
                }
                .catch {
                    _archiveDiary.value = Result.Error(it.message.orEmpty())
                }
                .collect {
                    _archiveDiary.value = Result.Success(it.toDomain())
                }
        }
    }

    private val _unarchiveDiary = SingleLiveEvent<Result<Diary>>()
    val unarchiveDiary: LiveData<Result<Diary>> by lazy { _unarchiveDiary }

    fun unarchiveDiary(diaryID: String){
        viewModelScope.launch {
            diaryRepository
                .unarchiveDiary(diaryID)
                .onStart {
                    _unarchiveDiary.value = Result.Loading
                }
                .catch {
                    _unarchiveDiary.value = Result.Error(it.message.orEmpty())
                }
                .collectLatest{
                    _unarchiveDiary.value = Result.Success(it.toDomain())
                }
        }
    }

    private val _updateDiary = SingleLiveEvent<Result<Boolean>>()
    val updateDiary: LiveData<Result<Boolean>> by lazy { _updateDiary }

    fun updateDiary(diaryID: String, title: String, content: String){
        viewModelScope.launch {
            diaryRepository
                .updateDiary(diaryID, InsertUpdateDiaryRequest(title = title, content = content))
                .onStart {
                    _updateDiary.value = Result.Loading
                }
                .catch {
                    _updateDiary.value = Result.Error(it.message.orEmpty())
                }
                .collectLatest{
                    _updateDiary.value = Result.Success(it)
                }
        }
    }

    private val _isDiaryListNeedRefresh = MutableStateFlow(false)
    val isDiaryListNeedRefresh: StateFlow<Boolean> by lazy { _isDiaryListNeedRefresh }

    fun refreshTrigger(){
        _isDiaryListNeedRefresh.value = !_isDiaryListNeedRefresh.value
    }
}