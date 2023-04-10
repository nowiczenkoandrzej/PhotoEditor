package com.nowiczenkoandrzej.imagecropper.feature_picture_details.presentation

import androidx.lifecycle.ViewModel
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PictureDetailViewModel @Inject constructor(
    repository: PictureRepository
): ViewModel() {

    private val _state = MutableStateFlow(PictureItem())
    val state = _state.asStateFlow()

    fun onEvent(event: PictureDetailEvent) {
        when(event) {
            is PictureDetailEvent.EnterScreen -> setState(picture = event.picture)
            is PictureDetailEvent.EditTitle -> updateTitle(title = event.title)
        }
    }

    private fun updateTitle(title: String) {
        _state.value.copy()
    }

    private fun setState(picture: PictureItem) {
        _state.value = picture
    }


}