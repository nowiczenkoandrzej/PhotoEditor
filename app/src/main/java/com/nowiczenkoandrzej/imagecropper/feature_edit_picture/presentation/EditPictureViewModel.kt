package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureModel
import com.nowiczenkoandrzej.imagecropper.core.domain.repository.PictureRepository
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.FilterType
import com.nowiczenkoandrzej.imagecropper.feature_edit_picture.util.PictureFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditPictureViewModel @Inject constructor(
    private val repository: PictureRepository
): ViewModel() {


    private val _originalPicture = MutableStateFlow<Bitmap?>(null)
    private val _editedPicture = MutableStateFlow(PictureModel())
    val editedPicture = _editedPicture.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()


    fun onEvent(event: EditPictureEvent) {
        when(event){
            is EditPictureEvent.AddFilter -> addFilter(filterType = event.filterType)
            is EditPictureEvent.SavePicture -> savePicture(picture = event.picture)
            is EditPictureEvent.SetPictureToEdit -> setPictureToEdit(picture = event.picture)
        }
    }

    private fun setPictureToEdit(picture: PictureModel){
        _editedPicture.value = picture
        _originalPicture.value = picture.originalBitmap
    }


    private fun savePicture(picture: PictureModel){
        val time = LocalDate.now()

        val finalPicture = PictureModel(
            editedBitmap = picture.editedBitmap,
            originalBitmap = _originalPicture.value,
            title = picture.title,
            lastEdit = time
        )
        viewModelScope.launch {
            _isLoading.value = true
            repository.insertPicture(finalPicture)
            _isLoading.value = false
        }
    }


    private fun addFilter(filterType: FilterType){

        val originalBitmap = _originalPicture.value ?: return
        val pf = PictureFilter(bitmap = originalBitmap)

        when(filterType) {
            is FilterType.BlackAndWhite -> _editedPicture.value = PictureModel(editedBitmap = pf.blackAndWhitePicture(), originalBitmap = originalBitmap)
            is FilterType.Invert -> _editedPicture.value = PictureModel(editedBitmap = pf.invertPicture(), originalBitmap = originalBitmap)
            is FilterType.Normal -> _editedPicture.value = PictureModel(editedBitmap = originalBitmap, originalBitmap = originalBitmap)
            is FilterType.Sepia -> _editedPicture.value = PictureModel(editedBitmap = pf.sepiaPicture(), originalBitmap = originalBitmap)
        }
    }



}