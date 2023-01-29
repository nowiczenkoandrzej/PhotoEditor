package com.nowiczenkoandrzej.imagecropper.presentation.add_picture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenkoandrzej.imagecropper.domain.model.AddPictureState
import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.domain.repository.PictureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPictureViewModel @Inject constructor(
    private val repository: PictureRepository
): ViewModel() {

    private val TAG = "VIEW_MODEL"

    private val _state = MutableStateFlow(AddPictureState())
    val state = _state.asStateFlow()

    fun onEvent(event: AddPictureEvent){

        when(event){
            is AddPictureEvent.ChoosePicture -> choosePicture()
            is AddPictureEvent.PictureCropped -> pictureCropped(event.picture)
            is AddPictureEvent.SavePicture -> savePicture(picture = event.picture)
            is AddPictureEvent.BackFromChoosingPicture -> backFromChoosingPicture()
        }
    }

    private fun choosePicture() {
        _state.value = AddPictureState(
            picture = state.value.picture,
            chooseImageToCrop = true,
            textedPicture = state.value.textedPicture
        )
    }

    private fun backFromChoosingPicture(){
        _state.value = AddPictureState(
            picture = state.value.picture,
            chooseImageToCrop = false,
            textedPicture = state.value.textedPicture
        )
    }

    private fun pictureCropped(picture: PictureModel) {
        _state.value = AddPictureState(
            picture = picture,
            chooseImageToCrop = false,
            textedPicture = state.value.textedPicture
        )
    }

    private fun savePicture(picture: PictureModel) {
        viewModelScope.launch {
            repository.insertPicture(picture)
        }
    }

}