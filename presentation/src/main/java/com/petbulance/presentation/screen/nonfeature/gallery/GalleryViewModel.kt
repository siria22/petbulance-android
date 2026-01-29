package com.petbulance.presentation.screen.nonfeature.gallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val contentResolver = application.contentResolver

    // Paging Data Flow
    val galleryImages: Flow<PagingData<GalleryImage>> = Pager(
        config = PagingConfig(pageSize = 50, enablePlaceholders = false),
        pagingSourceFactory = { GalleryPagingSource(contentResolver) }
    ).flow.cachedIn(viewModelScope)

    // 선택된 이미지 상태 관리
    private val _selectedImages = MutableStateFlow<List<GalleryImage>>(emptyList())
    val selectedImages: StateFlow<List<GalleryImage>> = _selectedImages.asStateFlow()

    private var maxSelectionCount = 1

    fun setMaxSelectionCount(count: Int) {
        maxSelectionCount = count
    }

    fun toggleImageSelection(image: GalleryImage) {
        val currentList = _selectedImages.value.toMutableList()
        if (currentList.contains(image)) {
            currentList.remove(image)
        } else {
            if (maxSelectionCount == 1) {
                // 단일 선택 모드일 경우 교체
                currentList.clear()
                currentList.add(image)
            } else {
                // 다중 선택 모드일 경우 최대 개수 체크
                if (currentList.size < maxSelectionCount) {
                    currentList.add(image)
                }
            }
        }
        _selectedImages.value = currentList
    }

    fun clearSelection() {
        _selectedImages.value = emptyList()
    }
}