package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicturesListViewModel @Inject constructor(
    private val repository: PictureRepository
): ViewModel() {

    //private val _pictureList = repository.getPictures()
    val pictureList = repository.getPictures()

    /*init {
        refreshPictureList()
    }

    fun refreshPictureList() {
        viewModelScope.launch {
            repository.getPictures().collect { pictureState ->
                _pictureList.value = pictureState
            }
        }
    }*/

}