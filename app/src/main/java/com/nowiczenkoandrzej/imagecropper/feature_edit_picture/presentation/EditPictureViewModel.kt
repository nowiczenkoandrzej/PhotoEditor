package com.nowiczenkoandrzej.imagecropper.feature_edit_picture.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nowiczenkoandrzej.imagecropper.core.data.mappers.toBitmap
import com.nowiczenkoandrzej.imagecropper.core.domain.model.PictureItem
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

    private val _editedPicture = MutableStateFlow(PictureItem())
    val editedPicture = _editedPicture.asStateFlow()

    private val _editedBitmap = MutableStateFlow<Bitmap?>(null)
    val editedBitmap = _editedBitmap.asStateFlow()



    fun onEvent(event: EditPictureEvent) {
        when(event){
            is EditPictureEvent.AddFilter -> addFilter(filterType = event.filterType, context = event.context)
            is EditPictureEvent.SavePicture -> savePicture(picture = event.picture)
            is EditPictureEvent.SetPictureToEdit -> setPictureToEdit(picture = event.picture, context = event.context)
        }
    }

    private fun setPictureToEdit(picture: PictureItem, context: Context){
        _editedPicture.value = picture
        val pictureToEdit = Uri.parse(picture.picture)
        _editedBitmap.value = pictureToEdit.toBitmap(context)
    }


    private fun savePicture(picture: PictureItem){
        val time = LocalDate.now()

        val result = PictureItem(
            picture = picture.picture,
            originalPicture = picture.originalPicture,
            lastEdit = time,
            title = picture.title
        )
        viewModelScope.launch {
            repository.insertPicture(result)
        }
    }


    private fun addFilter(filterType: FilterType, context: Context){

        val originalUri = _editedPicture.value.originalPicture ?: return
        val pf = PictureFilter(uri = Uri.parse(originalUri), context)

        when(filterType) {
            is FilterType.BlackAndWhite -> _editedBitmap.value = pf.blackAndWhitePicture()
            is FilterType.Invert -> _editedBitmap.value = pf.invertPicture()
            is FilterType.Sepia -> _editedBitmap.value = pf.sepiaPicture()
            is FilterType.Normal -> {
                val uri = Uri.parse(_editedPicture.value.originalPicture)
                _editedBitmap.value = uri.toBitmap(context)

            }
        }
    }



}