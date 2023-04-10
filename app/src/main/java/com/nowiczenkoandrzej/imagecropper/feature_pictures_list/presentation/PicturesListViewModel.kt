package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.presentation

import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.PositionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicturesListViewModel @Inject constructor(
    private val repository: PictureRepository,
    private val dataStore: PositionManager
): ViewModel() {

    val pictureList = repository.getPictures()

    val position = dataStore.getPosition()

    fun setPosition(pos: Int) {
        viewModelScope.launch {
            dataStore.setPosition(pos)
        }
    }

}