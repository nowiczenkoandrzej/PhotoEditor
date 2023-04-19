package com.nowiczenkoandrzej.imagecropper.feature_pictures_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.feature_pictures_list.util.PositionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PicturesListViewModel @Inject constructor(
    private val repository: PictureRepository,
    private val dataStore: PositionManager
): ViewModel() {

    private val _pictureList = MutableStateFlow<List<PictureItem>>(emptyList())
    val pictureList = _pictureList.asStateFlow()

    val position = dataStore.getPosition()

    private var searchJob: Job? = null

    init {
        getPicturesByName("")
    }

    fun setPosition(pos: Int) {
        viewModelScope.launch {
            dataStore.setPosition(pos)
        }
    }

    fun getPicturesByName(name: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            repository.getPicturesByName(name).collect{ items ->
                _pictureList.value = items
            }

        }

    }

}