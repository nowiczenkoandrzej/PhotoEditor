package com.nowiczenkoandrzej.imagecropper.presentation.add_picture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenkoandrzej.imagecropper.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.domain.repository.PictureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPictureViewModel @Inject constructor(
    private val repository: PictureRepository
): ViewModel() {

    private val TAG = "VIEW_MODEL"

    private val _pictureState = MutableStateFlow(PictureModel())
    val pictureState = _pictureState.asStateFlow()

    private val _pickPictureToCrop = Channel<Boolean>()
    val pickPictureToCrop = _pickPictureToCrop.receiveAsFlow()

    fun onEvent(event: AddPictureEvent){

        when(event){
            is AddPictureEvent.ChoosePicture -> choosePicture()
            is AddPictureEvent.PictureCropped -> pictureCropped(event.picture)
            is AddPictureEvent.SavePicture -> savePicture(picture = event.picture)
            is AddPictureEvent.EnterActivity -> backFromChoosingPicture()
        }
    }

    private fun choosePicture() {
        viewModelScope.launch {
            _pickPictureToCrop.send(true)
        }

    }

    private fun backFromChoosingPicture(){
        viewModelScope.launch {
            _pickPictureToCrop.send(false)
        }
    }

    private fun pictureCropped(picture: PictureModel) {
        _pictureState.value = picture
    }

    private fun savePicture(picture: PictureModel) {
        viewModelScope.launch {
            repository.insertPicture(picture)
        }
    }

}